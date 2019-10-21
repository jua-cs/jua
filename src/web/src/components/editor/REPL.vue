<template>
    <Container :reset="reset">
        <codemirror :options="options" @ready="onCmReady"></codemirror>
    </Container>
</template>

<script>
    import {url} from '../../util';
    import Container from './Container'
    import {codemirror} from 'vue-codemirror'


    export default {
        name: 'REPL',
        components: {
            codemirror,
            Container
        },
        data() {
            return {
                cminstance: null,
                ws: null,
                options: {
                    electricChars: false,
                    theme: 'mdn-like',
                    mode: 'lua',
                    smartIndent: false,
                    lineWrapping: true,
                    gutters: ["CodeMirror-linenumber", "prompt"],
                    extraKeys: {
                        "Up": this.up,
                        "Down": this.down,
                        "Enter": this.enter,
                        "Shift-Enter": this.enter,
                    },
                    indentUnit: 4,
                    undoDepth: 1,
                    autofocus: true
                },
                history: [],
                historyIndex: 0,
                text: "",
                line: 0,
            }
        },
        created() {
            this.connect();
        },
        destroyed() {
            this.ws.close();
        },
        methods: {
            onCmReady: function (cm) {
                this.cminstance = cm;
            },
            setLine: function (line, text) {
                if (line >= 0 && line < this.cminstance.doc.size) {
                    this.cminstance.replaceRange(text, {line: line, ch: 0}, {
                        line: line,
                        ch: this.cminstance.getLine(line).length
                    });
                }
            },
            setMarker: function (line, text) {
                let element = document.createElement("p");
                element.innerText = text;
                this.cminstance.setGutterMarker(line, "CodeMirror-linenumber", element);
            },
            incrementLine: function () {
                this.cminstance.markText({line: 0, ch: 0}, {
                    line: this.line + 1,
                    ch: 0
                }, {readOnly: true});
                this.line++;
            },
            connect: function () {
                this.ws = new WebSocket(`ws://${url}/api/v1/repl`);
                this.ws.onopen = this.init;
                this.ws.onmessage = this.onMessage;
                this.ws.onclose = this.connect;
            },
            reset: function () {
                this.ws.close();
            },
            init: function () {
                this.cminstance.setValue('');
                this.history = [];
                this.historyIndex = 0;
                this.text = "";
                this.line = 0;
            },
            onMessage: function (message) {
                let payload = JSON.parse(message.data);
                if (typeof payload.return !== "undefined") {
                    if (payload.return === "> ") {
                        this.setMarker(this.line, ">>>");
                    } else {
                        this.print(payload.return);
                    }
                }
                if (typeof payload.error !== "undefined") {
                    this.print(payload.error, "redText");
                }
            },
            up: function () {
                if (this.historyIndex <= 0) {
                    this.historyIndex = 0;
                    return;
                }
                //If this is a new line, store it into the text buffer
                if (this.historyIndex === this.history.length) {
                    this.text = this.cminstance.getLine(this.line);
                }
                //Display an earlier line
                this.historyIndex--;
                this.setLine(this.line, this.history[this.historyIndex]);
            },

            down: function () {
                if (this.historyIndex >= this.history.length) {
                    return;
                }
                this.historyIndex++;

                //Display the line in the text buffer if this is the end of history...
                let text = this.text;
                //or the requested line in history
                if (this.historyIndex < this.history.length) {
                    text = this.history[this.historyIndex];
                }
                this.setLine(this.line, text);
            },
            enter: function () {
                let text = this.cminstance.getLine(this.line);

                this.historyIndex = this.history.push(text);
                this.setLine(this.line, text + '\n');
                this.incrementLine();
                this.ws.send(text + '\n');
            },

            print: function (message, className) {
                const ln = this.line;

                message = String(message);
                const text = this.cminstance.getLine(this.line);
                message = message.replace(/\n/g, '\r') + '\n';

                let cursor = 0;
                if (text) {
                    this.setMarker(this.line, "");
                    cursor = this.cminstance.getCursor().ch;
                }

                this.setLine(this.line, message);
                this.incrementLine();
                if (className) {
                    this.cminstance.markText({line: ln, ch: 0}, {line: ln, ch: message.length}, {className});
                }

                if (text) {
                    this.setLine(this.line, text);
                    this.cminstance.setCursor({line: this.line, ch: cursor});
                }
            },

        }
    }
</script>


<style>
    .redText {
        color: red !important;
    }
</style>
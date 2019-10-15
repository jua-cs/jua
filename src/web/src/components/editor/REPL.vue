<template>
    <div class="editor-container">
    <div class="prompt"> {{prompt}}</div>
    <input v-model="code" v-on:keyup.enter="run()">
    </div>
</template>

<script>
    import {url} from '../../util';

    export default {
        name: 'REPL',
        data() {
            return {
                code: "",
                prompt: "",
                ws: null,
            }
        },
        created() {
            this.ws = new WebSocket(`ws://${url}/repl`);
            this.ws.onmessage = this.updatePrompt;
        },
        methods: {
            run: function () {
                this.ws.send(this.code + '\n');
                this.prompt += this.code + '\n';
                this.code = "";
            },
            updatePrompt: function(message) {
                this.prompt += message.data;
            }
        }
    }
</script>

<style scoped>
    .prompt {
        white-space: pre;
    }
</style>

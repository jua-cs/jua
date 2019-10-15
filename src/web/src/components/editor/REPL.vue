<template>
    <Container :reset="reset">
        <pre class="prompt"> {{prompt}}</pre>
        <input v-model="code" v-on:keyup.enter="run()">
    </Container>
</template>

<script>
    import {url} from '../../util';
    import Container from './Container'

    export default {
        name: 'REPL',
        components: {
            Container
        },
        data() {
            return {
                code: "",
                prompt: "",
                ws: null,
            }
        },
        created() {
            this.connect();
        },
        destroyed() {
            this.ws.close();
        },
        methods: {
            run: function () {
                this.ws.send(this.code + '\n');
                this.prompt += this.code + '\n';
                this.code = "";
            },
            connect: function() {
                this.ws = new WebSocket(`ws://${url}/api/v1/repl`);
                this.ws.onmessage = this.updatePrompt;
            },
            reset: function() {
                this.ws.close();
                this.code = "";
                this.prompt = "";
                this.connect();
            },
            updatePrompt: function (message) {
                this.prompt += message.data;
            }
        }
    }
</script>

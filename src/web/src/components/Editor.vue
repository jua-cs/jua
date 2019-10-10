<template>
    <div class="container">
        <div class="header">
            <button v-on:click="run"> Run</button>
            <button v-on:click="reset"> Reset</button>
        </div>
        <div class="editor">
            <codemirror :options="cmOptions" class="code" v-model="code"></codemirror>
            <pre class="result"> > {{ exec }}</pre>
        </div>
    </div>
</template>

<script>
    import {codemirror} from 'vue-codemirror'
    // Themes
    import 'codemirror/lib/codemirror.css'
    import 'codemirror/theme/monokai.css'
    // Lua support
    import 'codemirror/mode/lua/lua.js'

    const startingCode = "print(\"Hello and welcome to Jua !\")";

    export default {
        name: 'Editor',
        props: {
            msg: String
        },
        components: {
            codemirror
        },
        data() {
            return {
                code: startingCode,
                exec: '',
                cmOptions: {
                    tabSize: 4,
                    mode: 'lua',
                    theme: 'monokai',
                    lineNumbers: true,
                    line: true,
                    lineWrapping: true
                }
            }
        },
        methods: {
            run: function () {
                this.exec = this.code;
                // TODO send code to backend
            },
            reset: function () {
                this.exec = ''
                this.code = startingCode
            }
        }
    }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
    .container {
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        height: 100%;
        background-color: #272822;
    }


    .editor {
        display: flex;
        border-top: thin solid white;
        height: 90%;
    }

    .code {
        width: 60%;
    }

    .result {
        background-color: #272822;
        border-left: thin solid white;
        width: 40%;
        height: 100%;
        color: white;
        margin: 0;
        overflow: auto;
    }

    .header {
        display: flex;
        flex-direction: row;
        justify-content: space-around;
        align-items: center;
        height: 10%;
    }

</style>

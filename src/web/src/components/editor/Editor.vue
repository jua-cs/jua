<template>
    <div class="editor-container">
        <Header :run="run" :reset="reset" />
        <div class="editor">
            <codemirror :options="cmOptions" class="code" v-model="code"></codemirror>
            <pre :class="{ redText: error }" class="result">{{ result }}</pre>
        </div>
    </div>
</template>

<script>
    import {codemirror} from 'vue-codemirror'
    import axios from 'axios';
    // Themes
    import 'codemirror/lib/codemirror.css'
    import 'codemirror/theme/mdn-like.css'
    // Lua support
    import 'codemirror/mode/lua/lua.js'

    import Header from "./Header";


    const startingCode = "print(\"Hello and welcome to Jua !\")";

    export default {
        name: 'Editor',
        components: {
            codemirror,
            Header
        },
        data() {
            return {
                code: startingCode,
                result: '',
                error: false,
                cmOptions: {
                    tabSize: 4,
                    mode: 'lua',
                    theme: 'mdn-like',
                    lineNumbers: true,
                    line: true,
                    lineWrapping: true,
                }
            }
        },
        methods: {
            run: async function () {
                try {
                    const res = await axios.post("/api/v1/interpreter", {
                        code: this.code
                    });
                    this.result = res.data;
                    this.error = false;
                } catch (e) {
                    this.result = "ERROR\n" + e.response.data.message;
                    this.error = true;
                }
            },
            reset: function () {
                this.exec = '';
                this.code = startingCode
            }
        }
    }
</script>

<style scoped>
    .editor-container {
        display: flex;
        flex-direction: column;
        height: 100%;
        background-color: #e1e1db;
    }


    .editor {
        display: flex;
        border: thick solid #bbbbbb;
        border-radius: 3px;
        height: 90%;
        margin: 0 1% 0.5% 1%;
    }

    .code {
        width: 60%;
    }

    .result {
        background-color: white;
        border-left: thin solid #bbbbbb;
        width: 40%;
        color: white;
        margin: 0;
        overflow: auto;
        padding: 8px;
        font-size: 14px;
    }

</style>

<style>
    .CodeMirror {
        height: 100%;
        font-size: 14px;
    }
</style>

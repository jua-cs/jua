<template>
    <div class="editor-container">
        <Header :reset="reset" :run="run"/>
        <div class="editor">
            <codemirror :options="cmOptions" class="code" v-model="code"></codemirror>
            <div class="result">
                <pre :class="{ redText: error }">{{ result }}</pre>
                <b-loading :active.sync="loading" :is-full-page="false"></b-loading>
            </div>
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
                loading: false,
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
                this.loading = true;
                try {
                    const res = await axios.post("/api/v1/interpreter", {
                        code: this.code
                    });
                    this.result = res.data;
                    this.error = false;
                    this.$buefy.toast.open({
                        message: `Success`,
                        type: 'is-success'
                    });
                } catch (e) {
                    this.$buefy.toast.open({
                        message: `An error occured: ${e.response.data.message}`,
                        type: 'is-danger'
                    });
                    this.this.result = "ERROR\n" + e.response.data.message;
                    this.error = true;
                }
                this.loading = false;
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
        margin: 0;
        overflow: auto;
        position: relative;
    }

    .result > pre {
        color: #222222;
        font-size: 14px;
        background-color: white;
        padding: 8px;
    }

    .redText {
        color: red;
    }
</style>

<style>
    .CodeMirror {
        height: 100%;
        font-size: 14px;
    }
</style>

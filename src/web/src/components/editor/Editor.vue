<template>
    <Container :reset="reset" :run="run" :share="share">
        <div class="editor">
            <codemirror :options="cmOptions" class="code" v-model="code"></codemirror>
            <div class="result">
                <pre :style="{ color: error ? 'red' : '#222222'}">{{ result }}</pre>
                <b-loading :active.sync="loading" :is-full-page="false"></b-loading>
            </div>
        </div>
    </Container>
</template>

<script>
    import {codemirror} from 'vue-codemirror'
    // Themes
    import 'codemirror/lib/codemirror.css'
    import 'codemirror/theme/mdn-like.css'
    // Lua support
    import 'codemirror/mode/lua/lua.js'

    import Container from './Container'

    const startingCode = "print(\"Hello and welcome to Jua !\")";
    import {url} from '../../util';

    export default {
        name: 'Editor',
        components: {
            codemirror,
            Container
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
                },
                decoder: new TextDecoder("utf-8")
            }
        },
        methods: {
            run: async function () {
                this.loading = true;
                this.result = '';
                this.error = false;
                try {
                    const res = await fetch(
                        `http://${url}/api/v1/interpreter`,
                        {
                            method: 'post',
                            body: JSON.stringify({
                                code: this.code
                            }),
                            headers: {
                                'Content-Type': 'application/json'
                            }
                        },
                    );
                    if (res.status !== 200) {
                        const raw = (await res.body.getReader().read()).value;
                        const decoded = JSON.parse(this.decoder.decode(raw));
                        throw decoded.message;
                    }
                    await this.pump(res.body.getReader());
                    this.$buefy.snackbar.open({
                        message: "Success",
                        type: "is-success"
                    });
                // Handle all the errors here
                } catch (err) {
                    const msg = err.message || err;
                    this.$buefy.snackbar.open({
                        message: `An error occurred: ${msg}`,
                        type: 'is-danger'
                    });
                    this.result = `ERROR\n${msg}`;
                    this.error = true;
                }
                this.loading = false;
            },
            reset: function () {
                this.result = '';
                this.error = false;
                this.code = startingCode;
            },
            share: function () {
                this.$buefy.snackbar.open({
                    message: "Not available yet !",
                    type: "is-warning"
                });
            },
            pump: async function (reader) {
                const {done, value} = await reader.read();
                if (done) {
                    // TODO; close ?
                    return;
                }
                this.result += this.decoder.decode(value);
                return this.pump(reader);
            }
        }
    }
</script>

<style scoped>
    .editor {
        display: flex;
        border: thick solid #bbbbbb;
        border-radius: 3px;
        height: 90%;
        margin: 0 1% 0.5% 1%;
    }

    @media only screen and (min-width: 1224px) {
        /*  Desktops */
        .code {
            width: 60%;
        }

        .result {
            width: 40%;
            border-left: thin solid #bbbbbb;
        }
    }

    @media only screen and (max-width: 1224px) {
        /* Others */
        .code {
            height: 60%;
        }

        .editor {
            flex-direction: column;
        }

        .result {
            height: 40%;
            border-top: thin solid #bbbbbb;
        }
    }

    .result {
        background-color: white;
        margin: 0;
        overflow: auto;
        position: relative;
    }

    .result > pre {
        font-size: 14px;
        background-color: white;
        padding: 8px;
        height: 100%;
        width: 100%;
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

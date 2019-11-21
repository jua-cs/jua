<template>
    <div class="header">
        <b-button :disabled="!run" @click="run()" icon-right="play" size="is-medium" type="is-warning">
                <span class="button-inner">
                    run
                </span>
        </b-button>
        <b-button @click="reset()" icon-right="replay" size="is-medium" type="is-light">
                <span class="button-inner">
                    reset
                </span>
        </b-button>
        <b-button :disabled="!save" @click="save()" icon-right="share" size="is-medium" type="is-light">
                <span class="button-inner">
                    Save
                </span>
        </b-button>
        <b-button @click="switchPage('/')" icon-right="code-tags" size="is-medium" type="is-link" v-if="isOnRepl">
                <span class="button-inner">
                    Editor
                </span>
        </b-button>
        <b-button @click="switchPage('/repl')" icon-right="console-line" size="is-medium" type="is-link" v-else>
                <span class="button-inner">
                    REPL
                </span>
        </b-button>
        <a class="github-link" href="https://github.com/jua-cs/jua">
            <b-icon icon="github-circle" size="is-medium"/>
        </a>
    </div>
</template>


<script>
    export default {
        name: 'Header',
        props: {
            reset: Function,
            run: Function,
            save: Function,
        },
        computed: {
            isOnRepl: function () {
                return this.$route.fullPath === "/repl";
            }
        },
        methods: {
            switchPage: function (to) {
                this.$router.push(to);
            }
        }
    }
</script>

<style scoped>
    .header {
        display: flex;
        align-items: center;
        padding: 12px 1%;
    }

    /* Add margin to every button except the first */
    .header > button {
        margin-right: 5px;
    }

    .header > button + button {
        margin: 0 5px;
    }

    .button-inner {
        text-transform: uppercase;
        font-size: 14px;
        font-weight: 600;
    }

    .github-link {
        margin-left: auto;
        text-decoration: none;
    }
</style>

<style>
    .button > span {
        display: flex;
    }
</style>

<template>
    <div class="editor-container">
        <Header :reset="reset" :run="run" :share="share"/>
        <slot></slot>
        <footer>
            <a :href="`https://github.com/jua-cs/jua/commit/${version}`">
                version: {{version}}
            </a>
        </footer>
    </div>
</template>

<script>
    import Header from "./Header";

    export default {
        name: 'Container',
        components: {
            Header
        },
        props: {
            reset: Function,
            run: Function,
            share: Function
        },
        data: function () {
            return {
                // This is injected by babel
                // eslint-disable-next-line
                version: GIT_COMMIT
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

    footer {
        padding: 0px 0 5px 0px;
        align-self: center;
        font-size: 12px;
        font-style: italic;
    }
</style>

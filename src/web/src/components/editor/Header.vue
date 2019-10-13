<template>
    <div class="header">
        <b-button @click="run" icon-right="play" size="is-medium" type="is-warning">
                <span class="button-inner">
                    run
                </span>
        </b-button>
        <b-button @click="reset" icon-right="replay" size="is-medium" type="is-light">
                <span class="button-inner">
                    reset
                </span>
        </b-button>
        <a href="https://github.com/jua-cs/jua" class="github-link">
            <b-icon  icon="github-circle" size="is-medium" />
        </a>
    </div>
</template>


<script>
    export default {
        name: 'Header',
        props: {
            reset: Function,
            run: Function
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
    .header > button + button {
        margin: 0 10px;
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

<template>
    <div class="editor-container">
        <Header :reset="reset" :run="run" :share="share"/>
        <slot></slot>
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
</style>

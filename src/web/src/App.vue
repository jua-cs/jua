<template>
  <div id="app">
    <Editor />
  </div>
</template>

<script>
import Editor from './components/Editor.vue'

export default {
  name: 'app',
  components: {
    Editor
  }
}
</script>

<style>
  body {
    margin: 0
  }
  #app {
    width: 100vw;
    height: 100vh;
  }
</style>

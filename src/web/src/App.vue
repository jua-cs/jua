<template>
  <div id="app">
    <Editor />
  </div>
</template>

<script>
import Editor from './components/editor/Editor.vue'

export default {
  name: 'app',
  components: {
    Editor
  }
}
</script>

<style>
  @import "https://cdn.materialdesignicons.com/2.5.94/css/materialdesignicons.min.css";
  body {
    margin: 0
  }
  #app {
    width: 100vw;
    height: 100vh;
  }
</style>

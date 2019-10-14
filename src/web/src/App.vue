<template>
  <div id="app">
    <div>
      toggle REPL: 
      <input type="checkbox" v-model="isREPL">
    </div>
    <REPL v-if="isREPL" />
    <Editor v-else/>
  </div>
</template>

<script>
import Editor from './components/editor/Editor.vue'
import REPL from './components/editor/REPL.vue'

export default {
  name: 'app',
  components: {
    Editor,
    REPL,
  },
  data() {
    return {
      isREPL: false,
    }
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

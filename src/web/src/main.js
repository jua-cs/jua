import Vue from 'vue'
import VueRouter from 'vue-router'
import App from './App.vue'
import {Button, Icon, Loading, Snackbar, Switch} from 'buefy'
import 'buefy/dist/buefy.css'
import Editor from './components/editor/Editor.vue';
import REPL from './components/editor/REPL.vue';

Vue.use(VueRouter);
Vue.use(Button);
Vue.use(Icon);
Vue.use(Loading);
Vue.use(Snackbar);
Vue.use(Switch);

Vue.config.productionTip = false

const router = new VueRouter({
    mode: 'history',
    base: __dirname,
    routes: [
        {path: '/', component: Editor},
        {path: '/repl', component: REPL},
    ]
});

new Vue({
    router,
    render: h => h(App),
}).$mount('#app');

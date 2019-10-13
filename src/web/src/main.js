import Vue from 'vue'
import App from './App.vue'
import {Button, Icon, Loading, Toast} from 'buefy'
import 'buefy/dist/buefy.css'

Vue.use(Button);
Vue.use(Icon);
Vue.use(Loading);
Vue.use(Toast);

Vue.config.productionTip = false

new Vue({
    render: h => h(App),
}).$mount('#app');

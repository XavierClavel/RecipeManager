/**
 * main.ts
 *
 * Bootstraps Vuetify and other plugins then mounts the App`
 */

// Plugins
import { registerPlugins } from '@/plugins'

// Components
import App from './App.vue'

// Composables
import { createApp } from 'vue'
import { createPinia } from 'pinia';
import router from "@/router";
import { createI18n } from 'vue-i18n';
import en from './locales/en';
import fr from './locales/fr';

const pinia = createPinia();

const i18n = createI18n({
  locale: 'fr', // Default language
  fallbackLocale: 'en', // Fallback if a translation is missing
  messages: {
    en,
    fr
  }
});


const app = createApp(App)
app.use(pinia);
app.use(i18n)
app.use(router); // Add the router to the app
registerPlugins(app)
app.mount('#app')

import { createI18n } from 'vue-i18n'
import en from "@/locales/en";
import fr from "@/locales/fr";
import {getBrowserLocale} from "@/scripts/localization";


const i18n = createI18n({
  locale: getBrowserLocale(),
  fallbackLocale: 'en',
  messages: {
    en,
    fr
  }
});


export default i18n

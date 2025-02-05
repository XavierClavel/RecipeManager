import axios from "axios";
import router from "@/router";
import apiClient from '@/plugins/axios.js';
import {useAuthStore, declareLogin} from "@/stores/auth";
import i18n from '@/plugins/i18n'
import {ref} from "vue";
import {getCookie, setCookie} from "@/scripts/cookies";

export {
  overrideLocaleFromCookie,
  forceLocale,
  getLocale,
  getBrowserLocale,
  getInitialLocale,
}


function overrideLocaleFromCookie() {
  const cookieLocale = getCookie('locale')
  console.log(cookieLocale)
  if (cookieLocale) {
    setLocale(cookieLocale)
  }
  console.log("Current locale: ", getLocale())
}

function forceLocale(locale) {
  if (!Object.keys(i18n.global.messages).includes(locale)) {
    return
  }
  if (locale == getLocale()) {
    return
  }
  setCookie('locale', locale)
  setLocale(locale)
}

function setLocale(locale) {
  i18n.global.locale = locale
}

function getLocale() {
  let locale = i18n.global.locale
  if (!Object.keys(i18n.global.messages).includes(locale)) {
    locale = i18n.global.fallbackLocale
  }
  return locale
}

function getBrowserLocale() {
  const locale = navigator.language || navigator.languages[0] || 'en';
  return locale.split('-')[0]; //Handles regional variants ex en-US, fr-FR
}

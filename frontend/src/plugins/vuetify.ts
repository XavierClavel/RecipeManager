/**
 * plugins/vuetify.ts
 *
 * Framework documentation: https://vuetifyjs.com`
 */

// Styles
import '@mdi/font/css/materialdesignicons.css'
import 'vuetify/styles'

// Composables
import {createVuetify, ThemeDefinition} from 'vuetify'
import { VNumberInput } from 'vuetify/labs/VNumberInput'

const customTheme: ThemeDefinition = {
  dark: true,
  colors: {
    background: '#43aa8b',
    surface: '#254441',
    primary: '#ff6f59',
    secondary: '#f4b634',
    error: '#e5d352',
    info: '#2196F3',
    success: '#4CAF50',
    warning: '#FB8C00',
  },
}

// https://vuetifyjs.com/en/introduction/why-vuetify/#feature-guides
export default createVuetify({
  theme: {
    defaultTheme: 'customTheme',
    themes: {
      customTheme,
    }
  },
  defaults: {
    VCard: {
      rounded: 'lg',
    },
    VBtn: {
      rounded: 'lg',
    },
    VNavigationDrawer: {
      rounded: 'lg',
    },
  },
  components: {
    VNumberInput,
  }
})

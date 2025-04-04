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
    background: '#f0f4ef',
    surface: '#629677',
    primary: '#ff6f59',
    secondary: '#f4b634',
    error: '#ff6f59',
    info: '#2196F3',
    success: '#4CAF50',
    warning: '#FB8C00',
    'on-surface': "#000000", // Default text color
    'menu': "#0476a3"
  },
  variables: {
    fontFamily: 'Geologica, sans-serif',
  }
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
    VCardTitle: {
      class: "text-black text-h2 font-weight-bold !important",
    },
    VCard: {
      rounded: 'lg',
    },
    VBtn: {
      rounded: 'lg',
    },
    VNavigationDrawer: {
      rounded: 'lg',
    },
    VNumberInput: {
      VBtn: {
        rounded: 0,
      },
    },
  },
  components: {
    VNumberInput,
  },
  styles: {
    configFile: 'src/styles/settings.scss'
  }
})

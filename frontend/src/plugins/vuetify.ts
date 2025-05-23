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
    error: '#000000',
    info: '#2196F3',
    success: '#4CAF50',
    warning: '#FB8C00',
    'on-surface': "#000000", // Default text color
    'menu': "#0476a3",
    'border': "#0d1821",
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
      class: "text-black text-pre-wrap font-weight-bold text-h2 text-sm-h4",
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
      variant:"solo",
      flat: true,
      hideDetails: 'auto',
      bgColor: "background",
      color: "primary",
      VBtn: {
        rounded: 0,
      },
    },
    VTextField: {
      variant:"solo",
      flat: true,
      hideDetails: 'auto',
      bgColor: "background",
      color: "primary",
      class: "my-2"
    },
    VTextarea: {
      variant:"solo",
      flat: true,
      hideDetails: 'auto',
      bgColor: "background",
      color: "primary",
    },
    VSelect: {
      variant:"solo",
      flat: true,
      hideDetails: true,
      bgColor: "background",
      color: "primary",
      menuProps: {
        contentClass: 'custom-select-menu'
      },
      class: "custom-list",
    },
    VCombobox: {
      variant:"solo",
      flat: true,
      hideDetails: true,
      bgColor: "background",
      color: "primary",
    },
    VAutocomplete: {
      variant:"solo",
      flat: true,
      hideDetails: true,
      bgColor: "background",
      color: "primary",
    },
    VListItem: {
      slim: true,
    },
    VList: {
      slim: true,
    },
    VChip: {
      variant: "elevated",
      color: "primary"
    },
    VSnackbar: {
      color: "surface",
    }
  },
  components: {
    VNumberInput,
  },
  styles: {
    configFile: 'src/styles/settings.scss'
  }
})

import {
  ICON_ALPHABETICAL,
  ICON_AMOUNT,
  ICON_DATE,
  ICON_LIKES, ICON_NONE,
  ICON_RANDOM, ICON_VISIBILITY_PRIVATE, ICON_VISIBILITY_PROTECTED, ICON_VISIBILITY_PUBLIC,
  ICON_VOLUME,
  ICON_WEIGHT
} from "@/scripts/icons";
import {useI18n} from "vue-i18n";
import i18n from "@/plugins/i18n";

export {
  requiredRule,
  min8Rule,
  passwordRule,
  max511,
  max255,
  max100,
  max50,
}

const { t } = i18n.global


const requiredRule = value => !!value || t('required')
const min8Rule = v => v.length >= 8 || t('min_8_characters')
const max511 = v => v.length <= 511 || 'Max 511 characters'
const max255 = v => v.length <= 255 || 'Max 255 characters'
const max100 = v => v.length <= 100 || 'Max 100 characters'
const max50 = v => v.length <= 50 || 'Max 50 characters'
const passwordRule = (password1, password2) => password1 == password2 || t('passwords_must_match')

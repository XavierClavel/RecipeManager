<script setup lang="ts">
import {useI18n} from "vue-i18n";

const props = defineProps({
    action: {
      type: Function,
      required: true,
    },
    values: {
      type:[],
      required: true
    },
    selected: {
      type:[],
      required: false,
      default: []
    }
  }
)

const { t } = useI18n();

// Create a local copy of the prop
const selectedItems = ref([]);
selectedItems.value = props.selected

// Watch for external prop changes and update local state
watch(() => props.selected, (newValue) => {
  selectedItems.value = newValue;
});

// Emit updates when the user modifies the value
const emit = defineEmits(['update:selected']);
const updateValue = () => {
  emit('update:selected', selectedItems.value);
};


const selectItem = (index) => {
  if (selectedItems.value.includes(index)) {
    selectedItems.value.splice(selectedItems.value.indexOf(index),1)
  } else {
    selectedItems.value.push(index)
  }
  updateValue()
  props.action()
}
</script>

<template>
  <v-row class="d-flex flex-row ma-1">
    <v-chip
      v-for="(item, index) in values"
      filter
      class="mx-1"
      :color="selectedItems.includes(index) ? 'primary' : 'background'"
      variant="elevated"
      @click="selectItem(index)"
      :prepend-icon="selectedItems.includes(index) ? 'mdi-check' : undefined"
    > {{$t(item.label)}}</v-chip>
  </v-row>
</template>

<style scoped>

</style>




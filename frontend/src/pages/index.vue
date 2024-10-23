<template>
  <v-layout class="rounded rounded-md">
    <v-card
      :color="color"
      :variant="variant"
      class="mx-auto"
    >
      <v-card-item>
        <div>
          <div class="text-overline mb-1">
            {{ variant }}
          </div>
          <div class="text-h6 mb-1">
            Headline
          </div>
          <div class="text-caption">Greyhound divisely hello coldly fonwderfully</div>
        </div>
      </v-card-item>

      <v-card-actions>
        <v-btn>
          Button
        </v-btn>
      </v-card-actions>
    </v-card>


    <v-main class="d-flex align-center justify-center" style="min-height: 300px;" >
      Main Content
    </v-main>
  </v-layout>
</template>

<script lang="ts" setup>

</script>

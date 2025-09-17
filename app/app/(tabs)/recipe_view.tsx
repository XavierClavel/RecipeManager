import { Image } from 'expo-image';
import {FlatList, Platform, StyleSheet, Text, View} from 'react-native';

import ParallaxScrollView from '@/components/ParallaxScrollView';
import { ThemedText } from '@/components/ThemedText';
import { ThemedView } from '@/components/ThemedView';
import { IconSymbol } from '@/components/ui/IconSymbol';
import {getRecipe, RecipeInfo} from "@/scripts/recipes";
import React, {useEffect, useState} from "react";

const recipeId = 2


const RecipeDisplay = (props: RecipeInfo) => {
    return(
        <View>
            <ThemedText>{props.title}</ThemedText>
            <ThemedText>{props.description}</ThemedText>
            <FlatList data={props.steps} renderItem={({item}) => <ThemedText>{item}</ThemedText> }/>
        </View>
    )
}

export default function TabTwoScreen() {
    return (
    <ParallaxScrollView
      headerBackgroundColor={{ light: '#D0D0D0', dark: '#353636' }}
      headerImage={
        <IconSymbol
          size={310}
          color="#808080"
          name="chevron.left.forwardslash.chevron.right"
          style={styles.headerImage}
        />
      }>
      <ThemedView style={styles.titleContainer}>
        <ThemedText type="title">Recipe</ThemedText>
      </ThemedView>
    <RecipeDisplay
        id={1}
        version={1}
        title={"title"}
        dishClass={''}
        owner={{id: 1, role: '', version: 2, username: ''}}
        description={'Description goes here'}
        preparationTime={1}
        cookingTime={3}
        cookingTemperature={2}
        ingredients={[]}
        customIngredients={[]}
        steps={[]}
        tips={''}
        creationDate={1}
        editionDate={2}
        likesCount={23}
    />
    </ParallaxScrollView>
  );
}

const styles = StyleSheet.create({
  headerImage: {
    color: '#808080',
    bottom: -90,
    left: -35,
    position: 'absolute',
  },
  titleContainer: {
    flexDirection: 'row',
    gap: 8,
  },
});

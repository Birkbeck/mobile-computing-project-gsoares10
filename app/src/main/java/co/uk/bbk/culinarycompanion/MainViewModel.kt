package co.uk.bbk.culinarycompanion

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val _recipes = MutableLiveData(listOf<Recipe>())
    val recipes: LiveData<List<Recipe>> = _recipes

    var recipesDao: RecipesDao? = null

    fun readAllRecipes() {
        viewModelScope.launch {
            recipesDao?.let {
                val recipes = it.getAllRecipes()

                Log.i("BBK", recipes.toString())
                _recipes.value = recipes
            }
        }
    }

    fun addRecipe(title: String,
                  category: String,
                  ingredients: String,
                  instructions: String) {
        viewModelScope.launch {
            recipesDao?.let {
                val recipe = Recipe(title = title, category = category, ingredients = ingredients, instructions = instructions)
                it.insertRecipe(recipe)

                readAllRecipes()
            }
        }
    }

    fun editRecipe(recipe: Recipe) {
        viewModelScope.launch {
            recipesDao?.let {
                it.updateRecipe(recipe)

                readAllRecipes()
            }
        }
    }

    fun deleteRecipe(recipe: Recipe) {
        viewModelScope.launch {
            recipesDao?.let {
                it.deleteRecipe(recipe)

                readAllRecipes()
            }
        }
    }

}
package co.uk.bbk.culinarycompanion

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import co.uk.bbk.culinarycompanion.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val adapter = RecipeAdapter()
        binding.recyclerView.adapter = adapter


        val dao = RecipesDatabase.getInstance(applicationContext).recipesDao()
        viewModel.recipesDao = dao
        viewModel.readAllRecipes()
        viewModel.recipes.observe(this) { recipes ->
            adapter.updateRecipes(recipes)
        }
    }



}
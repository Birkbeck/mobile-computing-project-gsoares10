package co.uk.bbk.culinarycompanion

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
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
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.buttonAddRecipe.setOnClickListener {
            val intent = Intent(this, EditRecipeActivity::class.java)
            startActivity(intent)
        }


        val dao = RecipesDatabase.getInstance(applicationContext).recipesDao()
        viewModel.recipesDao = dao
        viewModel.readAllRecipes()
        viewModel.recipes.observe(this) { recipes ->
            adapter.updateRecipes(recipes)
        }
    }



}
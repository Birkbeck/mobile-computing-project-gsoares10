package co.uk.bbk.culinarycompanion

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import co.uk.bbk.culinarycompanion.databinding.ActivityMainBinding
import com.google.android.material.appbar.MaterialToolbar

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

        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)
        toolbar.setOnMenuItemClickListener { item ->
            val category = when (item.itemId) {
                R.id.filter_all -> "All"
                R.id.filter_breakfast -> "Breakfast"
                R.id.filter_brunch -> "Brunch"
                R.id.filter_lunch -> "Lunch"
                R.id.filter_dinner -> "Dinner"
                R.id.filter_desserts -> "Desserts"
                R.id.filter_other -> "Other"
                else -> return@setOnMenuItemClickListener false
            }
            viewModel.filterRecipesByCategory(category)
            true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_category_filter, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filter_all -> {
                viewModel.readAllRecipes()
                true
            }
            R.id.filter_breakfast -> {
                viewModel.filterRecipesByCategory("Breakfast")
                true
            }
            R.id.filter_brunch -> {
                viewModel.filterRecipesByCategory("Brunch")
                true
            }
            R.id.filter_lunch -> {
                viewModel.filterRecipesByCategory("Lunch")
                true
            }
            R.id.filter_dinner -> {
                viewModel.filterRecipesByCategory("Dinner")
                true
            }
            R.id.filter_desserts -> {
                viewModel.filterRecipesByCategory("Desserts")
                true
            }
            R.id.filter_other -> {
                viewModel.filterRecipesByCategory("Other")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
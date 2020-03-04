package ru.skillbranch.devintensive.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.data.ChatType
import ru.skillbranch.devintensive.ui.adapters.ChatAdapter
import ru.skillbranch.devintensive.ui.adapters.ChatItemTouchHelperCallback
import ru.skillbranch.devintensive.ui.archive.ArchiveActivity
import ru.skillbranch.devintensive.ui.group.GroupActivity
import ru.skillbranch.devintensive.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var chatAdapter: ChatAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        // После заставки (splash screen) возвращаем для активити app-тему
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolbar()
        initViews()
        initViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint = "Введите имя пользователя"
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.handleSearchQuery(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.handleSearchQuery(newText)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
    }

    private fun initViews() {
        chatAdapter = ChatAdapter {
            if (it.id.toInt() == -1) {
                startActivity(Intent(this, ArchiveActivity::class.java))
//                Snackbar.make(rv_chat_list,"Кликнут архив", Snackbar.LENGTH_LONG).show()
            }
            else Snackbar.make(rv_chat_list, "Click on ${it.title}",
                                Snackbar.LENGTH_LONG).show()
        }
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        // Если аргумент-лямбда последний, то его можно вынести за скобки
        val touchCallback = ChatItemTouchHelperCallback(chatAdapter) {
            val hadNotArchive = chatAdapter.items[0].chatType != ChatType.ARCHIVE
            viewModel.addToArchive(it.id)
            Snackbar.make(rv_chat_list, "Вы точно хотите добавить ${it.title} в архив?",
                Snackbar.LENGTH_LONG)
                .setAction("Undo", Listener(it.id, viewModel)).show()
            // Если список близок к началу и архивного айтема еще в списке не было
            if (it.id.toInt() < 15 && hadNotArchive)
                // Скроллим список вниз, чтобы показать на экране архивный айтем
                rv_chat_list.post { rv_chat_list.smoothScrollToPosition(0) }
        }
        val touchHelper = ItemTouchHelper(touchCallback)
        touchHelper.attachToRecyclerView(rv_chat_list)

        with(rv_chat_list) {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(divider)
        }

        fab.setOnClickListener {
            // TODO: implement me
            val intent = Intent(this, GroupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.getChatData().observe(this, Observer {
            chatAdapter.updateData(it)
        })
    }

    class Listener(val id: String, private val vm: MainViewModel)
                                        : View.OnClickListener {
        override fun onClick(v: View?) {
            vm.restoreFromArchive(id)
        }
    }
}

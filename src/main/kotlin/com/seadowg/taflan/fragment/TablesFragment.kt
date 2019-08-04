package com.seadowg.taflan.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.github.salomonbrys.kodein.KodeinInjector
import com.github.salomonbrys.kodein.instance
import com.seadowg.taflan.R
import com.seadowg.taflan.TaflanApplication
import com.seadowg.taflan.domain.Table
import com.seadowg.taflan.repository.TableRepository
import com.seadowg.taflan.util.Navigator
import com.seadowg.taflan.view.TableItem
import kotlinx.android.synthetic.main.launch.*

class TablesFragment : Fragment() {

    private val injector = KodeinInjector()

    private val tableRepository: TableRepository by injector.instance()

    private val navigator by lazy { Navigator(activity!!) }
    private val tablesViewModel by lazy { createViewModel(this) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        injector.inject((context.applicationContext as TaflanApplication).kodein)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.launch, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tablesViewModel.tables().observe(this, Observer {
            tables.removeAllViews()

            it.forEach { table ->
                val tableItem = TableItem.inflate(table, tables, context!!)

                tableItem.setOnClickListener {
                    navigator.showTable(table)
                }

                tables.addView(tableItem)
            }
        })

        fab.setOnClickListener { navigator.newTable() }
    }

    override fun onResume() {
        super.onResume()
        tablesViewModel.refresh()
    }

    private fun createViewModel(fragment: Fragment): TablesViewModel {
        val factory = TablesViewModelFactory(tableRepository)
        return ViewModelProviders.of(fragment, factory).get(TablesViewModel::class.java)
    }

}

class TablesViewModel(private val tableRepository: TableRepository) : ViewModel() {

    private val tables = MutableLiveData<List<Table.Existing>>()

    fun tables(): LiveData<List<Table.Existing>> {
        refresh()
        return tables
    }

    fun refresh() {
        tables.value = tableRepository.fetchAll()
    }
}

class TablesViewModelFactory(private val tableRepository: TableRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TablesViewModel(tableRepository) as T
    }
}

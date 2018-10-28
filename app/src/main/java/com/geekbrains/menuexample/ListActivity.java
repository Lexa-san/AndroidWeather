package com.geekbrains.menuexample;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private List<String> elements;
    private ArrayAdapter<String> adapter;
//    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        //Создаем массив элементов для списка
        elements = new ArrayList<String>();
        for(int i = 0; i < 5; i++) {
            elements.add("Element " + i);
        }

        // Связываемся с ListView
        ListView listView = findViewById(R.id.list);
        // создаем адаптер
        adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, elements);
        // устанавливаем адаптер списку
        listView.setAdapter(adapter);

        // регистрируем контекстное меню на список
//        registerForContextMenu(listView);

        /**
         * register CAB
         */
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            private ArrayList<Integer> selectedPositions = new ArrayList<Integer>(0);

            // do something when items are selected/de-selected
            @Override
            public void onItemCheckedStateChanged(android.view.ActionMode mode, int position, long id, boolean checked) {
                selectedPositions.add(position);
            }

            // Inflate the menu for the CAB
            @Override
            public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
                mode.setTitle("My CAB menu");
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.context_menu, menu);
                return true;
            }

            // Here you can perform updates to the CAB due to
            // an invalidate() request
            @Override
            public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
                return false;
            }

            // Respond to clicks on the actions in the CAB
            @Override
            public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_delete:
                        Toast.makeText(getBaseContext(), "'Delete' was clicked", Toast.LENGTH_SHORT).show();
                        deleteSelectedItems(selectedPositions);
                        selectedPositions.clear();
                        mode.finish(); // Action picked, so close the CAB
                        return true;
                    default:
                        return false;
                }
            }

            // Here you can make any necessary updates to the activity when
            // the CAB is removed. By default, selected items are deselected/unchecked.
            @Override
            public void onDestroyActionMode(android.view.ActionMode mode) {

            }
        });

    }

    private void deleteSelectedItems(ArrayList<Integer> selectedPositions) {
        int size = selectedPositions.size();
        for (int i = 0; i < size; i++) {
            deleteElement(selectedPositions.get(i));
        }
    }


    /**
     * Переопределение метода создания меню.
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Метод вызывается по нажатию на любой пункт меню.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                addElement();
                return true;
            case R.id.menu_clear:
                clearList();
                return true;
            case R.id.menu_exit:
                exitApp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Создание контекстного меню (context menu). Вызывается каждый раз перед показом context menu.
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    /**
     * Обработка нажатия на пункт контектсного меню (context menu).
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.menu_edit:
                Toast.makeText(this, "Menu Edit", Toast.LENGTH_SHORT).show();
                editElement(info.position);
                return true;
            case R.id.menu_delete:
                Toast.makeText(this, "Menu Delete", Toast.LENGTH_SHORT).show();
                deleteElement(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * Метод очищает лист полностью.
     */
    private void clearList() {
        elements.clear();
        adapter.notifyDataSetChanged();
    }

    /**
     * Метод добавляет элемент в список.
     */
    private void addElement() {
        elements.add("New element");
        adapter.notifyDataSetChanged();
    }

    /**
     * Метод переписывает текст пункта меню на другой.
     * @param id
     */
    private void editElement(int id) {
        elements.set(id, "Edited");
        adapter.notifyDataSetChanged();
    }

    /**
     * Метод удаляет пункт из меню.
     * @param id
     */
    private void deleteElement(int id) {
        elements.remove(id);
        adapter.notifyDataSetChanged();
    }

    /**
     * Exit from app.
     */
    private void exitApp() {
        finish();
    }

}

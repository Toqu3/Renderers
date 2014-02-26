package com.pedrogomez.renderers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.pedrogomez.renderers.exception.NullRendererBuiltException;

import java.util.Collection;

/**
 * Base adapter created to work RendererBuilders and Renderers. Other adapters have to use this one to create new lists.
 *
 * @author Pedro Vicente Gómez Sánchez.
 */
public class RendererAdapter<T> extends BaseAdapter {

    /*
     * Attributes
     */

    private LayoutInflater layoutInflater;
    private RendererBuilder<T> rendererBuilder;
    private AdapteeCollection<T> collection;


    /*
     * Constructor
     */

    public RendererAdapter(LayoutInflater layoutInflater, RendererBuilder rendererBuilder, AdapteeCollection<T> collection) {
        this.layoutInflater = layoutInflater;
        this.rendererBuilder = rendererBuilder;
        this.collection = collection;
    }

    /*
     * Implemented methods
     */

    @Override
    public int getCount() {
        return collection.size();
    }

    @Override
    public T getItem(int position) {
        return collection.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        T content = getItem(position);
        rendererBuilder.withContent(content);
        rendererBuilder.withConvertView(convertView);
        rendererBuilder.withParent(parent);
        rendererBuilder.withLayoutInflater(layoutInflater);
        Renderer<T> renderer = rendererBuilder.build();
        if (renderer == null) {
            throw new NullRendererBuiltException();
        }
        updateRendererExtraValues(content, renderer, position);
        renderer.render();
        return renderer.getRootView();
    }

    public void add(T element) {
        collection.add(element);
    }

    public void remove(T element) {
        collection.remove(element);
    }

    public void addAll(Collection<T> elements) {
        collection.addAll(elements);
    }

    public void removeAll(Collection<T> elements) {
        collection.removeAll(elements);
    }

    /**
     * Allows the client code to access the adaptee collection from subtypes of RendererAdapter.
     *
     * @return collection used in the adapter as the adaptee class.
     */
    protected AdapteeCollection<T> getCollection() {
        return collection;
    }

    /**
     * Empty implementation created to allow the client code to extend this class without override getView method.
     * This method is called before render the renderer.
     *
     * @param content  to be rendered.
     * @param renderer to be used to paint the content.
     * @param position of the content.
     */
    protected void updateRendererExtraValues(T content, Renderer<T> renderer, int position) {
        //Empty
    }

    /*
     * Recycle methods
     */

    @Override
    public int getItemViewType(int position) {
        T content = getItem(position);
        return rendererBuilder.getItemViewType(content);
    }

    @Override
    public int getViewTypeCount() {
        return rendererBuilder.getViewTypeCount();
    }
}

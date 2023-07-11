package com.travelsmartplus.travelsmartplus.adapters

/**
 * OnItemClickListener.
 * Use to pass click listener from adapter to Activity/Fragment
 *
 * @author Gabriel Salas
 */

interface OnItemClickListener<T> {
    fun onItemClick(item: T)
}
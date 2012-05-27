/*
 * Copyright (C) 2012 Rui Gonçalves
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.ruippeixotog.utils.android;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class SimpleListAdapter<T> extends BaseAdapter implements Filterable {

	protected Context context;
	protected List<T> objects;
	protected List<T> originalObjects;
	protected int rowId;

	protected LayoutInflater inflater;

	protected Filter filter;

	public SimpleListAdapter(Context context, int rowId, List<T> objects) {
		this.context = context;
		this.originalObjects = objects;
		this.objects = originalObjects;
		this.rowId = rowId;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public SimpleListAdapter(Context context, int rowId, T[] objects) {
		this(context, rowId, Arrays.asList(objects));
	}

	@Override
	public int getCount() {
		return objects.size();
	}

	@Override
	public T getItem(int position) {
		return objects.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getView(
				convertView == null ? inflater.inflate(rowId, parent, false)
						: convertView, objects.get(position),
				convertView == null);
	}

	protected View getView(View inflatedView, T object, boolean justInflated) {
		if (inflatedView instanceof TextView)
			((TextView) inflatedView).setText(object.toString());
		return inflatedView;
	}

	@Override
	public Filter getFilter() {
		if (filter == null)
			filter = new ObjectFilter();
		return filter;
	}

	protected boolean isFilterMatch(CharSequence constraint, T obj) {
		return obj != null
				&& obj.toString().toUpperCase()
						.startsWith(constraint.toString().toUpperCase());
	}

	private class ObjectFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			List<T> resultList = new ArrayList<T>();

			for (T obj : originalObjects)
				if (isFilterMatch(constraint, obj))
					resultList.add(obj);

			results.values = resultList;
			results.count = resultList.size();
			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			objects = (List<T>) results.values;
			if (results.count > 0)
				notifyDataSetChanged();
			else
				notifyDataSetInvalidated();
		}
	}
}

/*
 * Copyright (C) 2012 Rui Gonçalves and Daniel Cibrão
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

package net.ruippeixotog.pexplorer.app;

import net.ruippeixotog.pexplorer.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

public abstract class PermissionAction {

	public static final int DO_NOTHING = 0;
	public static final int WARN = 1;
	public static final int WARN_DANGEROUS = 2;

	private int labelId;
	private int descriptionId;
	private int warnLevel;

	private Dialog warningDialog;

	public PermissionAction(int labelId, int descriptionId, int warnLevel) {
		this.labelId = labelId;
		this.descriptionId = descriptionId;
		this.warnLevel = warnLevel;
	}

	public String getLabel(Context context) {
		return context.getString(labelId);
	}

	public String getDescription(Context context) {
		return context.getString(descriptionId);
	}

	public void execute(final Context context) {
		if (warnLevel >= WARN)
			getWarnDialog(context).show();
		else
			doAction(context);
	}

	protected abstract void doAction(Context context);

	private Dialog getWarnDialog(final Context context) {
		if (warningDialog == null
				|| !warningDialog.getContext().equals(context)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage(String.format(
					context.getString(R.string.dialog_msg_action_warn),
					getDescription(context)));
			builder.setCancelable(true);
			builder.setPositiveButton(R.string.continue_,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							doAction(context);
						}
					}).setNegativeButton(R.string.cancel, null);
			builder.setTitle(R.string.warning);
			builder.setIcon(android.R.drawable.ic_dialog_alert);
			warningDialog = builder.create();
		}
		return warningDialog;
	}
}

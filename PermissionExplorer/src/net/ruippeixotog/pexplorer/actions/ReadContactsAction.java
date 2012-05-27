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

package net.ruippeixotog.pexplorer.actions;

import java.util.Random;

import net.ruippeixotog.pexplorer.R;
import net.ruippeixotog.pexplorer.app.PermissionAction;
import net.ruippeixotog.pexplorer.entities.Contact;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.provider.ContactsContract;

public class ReadContactsAction extends PermissionAction {

	public ReadContactsAction() {
		super(R.string.read_contact_label, R.string.read_calendar_label,
				PermissionAction.DO_NOTHING);
	}

	@Override
	protected void doAction(final Context context) {

		Contact randomContact = getRandomContact(context);

		new AlertDialog.Builder(context)
				.setTitle(R.string.read_contact_title)
				.setMessage(
						String.format(context.getString(
								R.string.read_contact_entry,
								randomContact.getName(),
								randomContact.getPhoneNumber())))
				.setCancelable(true)
				.setPositiveButton(R.string.continue_,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
						}).show();
	}

	public static Contact getRandomContact(Context context) {
		Cursor cur = context.getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		int random = new Random().nextInt((cur.getCount() - 1) + 1);
		String name = null, phoneNumber = context
				.getString(R.string.phone_number_not_available);
		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {
				if (cur.getPosition() == random) {
					String id = cur.getString(cur
							.getColumnIndex(ContactsContract.Contacts._ID));
					name = cur
							.getString(cur
									.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					if (Integer
							.parseInt(cur.getString(cur
									.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
						Cursor pCur = context
								.getContentResolver()
								.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
										null,
										ContactsContract.CommonDataKinds.Phone.CONTACT_ID
												+ " = ?", new String[] { id },
										null);
						while (pCur.moveToNext()) {
							phoneNumber = pCur
									.getString(pCur
											.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						}
						pCur.close();
					}
					break;
				}
			}
		}
		return new Contact(name, phoneNumber, "");
	}
}

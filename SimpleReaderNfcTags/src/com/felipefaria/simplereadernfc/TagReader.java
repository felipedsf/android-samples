package com.felipefaria.simplereadernfc;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.felipefaria.exception.ExceptionTagReader;

public class TagReader extends Activity {

	private NfcAdapter nfcAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tag_reader);
		try {
			init();
			nfcHander(getIntent());
		} catch (ExceptionTagReader e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}

	}

	private void init() throws ExceptionTagReader {
		nfcAdapter = verifyNfc(NfcAdapter.getDefaultAdapter(TagReader.this));
	}

	private NfcAdapter verifyNfc(NfcAdapter adapter) throws ExceptionTagReader {
		if (adapter == null) {
			throw new ExceptionTagReader("This device not support NFC!");
		}
		if (!adapter.isEnabled()) {
			startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
		}

		return adapter;
	}

	private void nfcHander(Intent intent) {
		String action = intent.getAction();

		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
			Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			final AlertDialog dialog = getDialog(getDecimal(tag.getId()));
			dialog.show();
			final Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					dialog.dismiss();
					timer.cancel();
				}
			}, 4000);
		}
	}

	private AlertDialog getDialog(long id) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(TagReader.this);
		dialog.setTitle(R.string.title_dialog);
		dialog.setMessage(R.string.tag_id + " " + id);
		return dialog.create();
	}

	private long getDecimal(byte[] bytes) {
		long result = 0;
		long factor = 1;
		for (int i = 0; i < bytes.length; ++i) {
			long value = bytes[i] & 0xffl;
			result += value * factor;
			factor *= 256l;
		}
		return result;
	}

}

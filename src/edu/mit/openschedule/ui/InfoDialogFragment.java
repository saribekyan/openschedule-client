package edu.mit.openschedule.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import edu.mit.openschedule.R;

public class InfoDialogFragment extends DialogFragment {

    public interface InfoDialogFragmentListener {
        public void onDialogPositiveClick(InfoDialogFragment dialog, String text);
        public void onDialogNegativeClick(InfoDialogFragment dialog);
    }
    
    private InfoDialogFragmentListener mListener;
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (InfoDialogFragmentListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement InfoDialogFragmentListener");
        }
    }
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.enter_info_dialog, null);
	    final EditText editText = (EditText) layout.findViewById(R.id.dialog_enter_text_id);
	    
	    int type = getArguments().getInt("dialog_type");
	    if (type == 0) {
	    	editText.setHint("Please enter submit location.");
	    } else {
	    	editText.setHint("How long did the assignment take?");
	    }
	    
	    
	    builder.setView(layout)
	    	.setPositiveButton("Done", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mListener.onDialogPositiveClick(InfoDialogFragment.this, editText.getText().toString());
				}
			})
			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mListener.onDialogNegativeClick(InfoDialogFragment.this);
				}
			});
	    
	    return builder.create();
	}
	
	public int getDialogType() {
		return getArguments().getInt("dialog_type");
	}
	
	public int getTaskId() {
		return getArguments().getInt("task_id");
	}
	
	public String getTaskName() {
        return getArguments().getString("task_name");
	}
}

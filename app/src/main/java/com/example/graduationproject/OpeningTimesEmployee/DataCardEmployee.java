package com.example.graduationproject.OpeningTimesEmployee;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.graduationproject.IP;
import com.example.graduationproject.Model.PostEmployee;
import com.example.graduationproject.R;
import com.example.graduationproject.buy_tickets;
import com.example.graduationproject.tickets_information;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataCardEmployee extends RecyclerView.Adapter<DataCardEmployee.ViewHolder> {

    List<PostEmployee> employeeList;
    Context context;
    IP ip = new IP();

    public DataCardEmployee(Context context, List<PostEmployee> postEmployees) {
        this.context = context;
        this.employeeList = postEmployees;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.data_card_employee,
                parent,
                false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        //I should change
        TextView dateTxtView = (TextView) cardView.findViewById(R.id.dateTxtEmp);
        TextView dataTxtView = (TextView) cardView.findViewById(R.id.dataTxtEmp);

        PostEmployee postEmployee = employeeList.get(position);
        dateTxtView.setText("التاريخ : "+postEmployee.getDate());
        dataTxtView.setText(postEmployee.getText());

        Button edit = (Button) cardView.findViewById(R.id.editEmBtn);
        edit.setOnClickListener(view -> {
            //start new activity
            Intent intent = new Intent(edit.getContext(), WritePostEmployeeActivity.class);
            intent.putExtra("idPost", postEmployee.getId() + "");
            intent.putExtra("prevDatePost", postEmployee.getDate());
            intent.putExtra("prevPost", postEmployee.getText());
            edit.getContext().startActivity(intent);
        });



        Button delete = (Button) cardView.findViewById(R.id.deleteEmBtn);
        delete.setOnClickListener(view -> {

            AlertDialog alertDialog1 = new AlertDialog.Builder(this.context)
                    .setIcon(R.drawable.ask)
                    .setTitle("تأكيد عملية الحذف")
                    .setMessage("هل تريد حذف المعلومة ؟")
                    .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deletePost(employeeList.get(position).getId());
                            employeeList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, employeeList.size());
                            Toast.makeText(context, "تم الحذف بنجاح" ,
                                    Toast.LENGTH_LONG).show();
                        }
                    })

                    .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                        }
                    })

                    .show();



        });


    }

    private void deletePost(int id) {
        String url = "http://"+ip.getIp().trim()+"/GraduationProject/deletePostByEm.php";
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //textTry.setText(error.getMessage());
                Toast.makeText(context,
                        "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                //by shared preference
                params.put("id", id + "");

                return params;
            }
        };
        queue.add(request);

    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;

        }

    }
}

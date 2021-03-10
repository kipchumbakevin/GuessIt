package com.guess.guessit.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.guess.guessit.AnswerQuestion;
import com.guess.guessit.R;
import com.guess.guessit.models.QuestionsModel;
import com.guess.guessit.utils.SharedPreferencesConfig;

import java.util.ArrayList;

public class GetQuestionsAdapter extends RecyclerView.Adapter<GetQuestionsAdapter.ViewHolder> {
    private final Context mContext;
    private final ArrayList<QuestionsModel> mQuestionsArray;
    private final LayoutInflater mLayoutInflator;
    private final SharedPreferencesConfig sharedPreferencesConfig;

    public GetQuestionsAdapter(Context context, ArrayList<QuestionsModel>questionsModels){
        mContext = context;
        mQuestionsArray = questionsModels;
        mLayoutInflator = LayoutInflater.from(mContext);
        sharedPreferencesConfig = new SharedPreferencesConfig(mContext);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflator.inflate(R.layout.question_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuestionsModel questionsModel = mQuestionsArray.get(position);

        holder.question.setText("Question "+questionsModel.getId());
        holder.id = Integer.toString(questionsModel.getId());
        holder.quest = questionsModel.getQuestion();
        holder.anser = questionsModel.getAnswer();
    }

    @Override
    public int getItemCount() {
        return mQuestionsArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView question;
        String id,quest,anser;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.question);

            question.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, AnswerQuestion.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("ID",id);
                    intent.putExtra("QUIZ",quest);
                    intent.putExtra("ANSWER",anser);
                    mContext.startActivity(intent);
                    
                }
            });
        }


    }
}

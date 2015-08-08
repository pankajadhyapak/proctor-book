package com.madproject.proctorbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.List;

public class SubjectListItem extends ArrayAdapter{
    private int mLastPosition;

    protected  Context mContext;
    String[] mSubjectsName;
    String[] mSubjectsCode;
    String[] mSubjectsGrade;

    public SubjectListItem(Context context, String[] SubjectsName, String[] SubjectsCode, String[] SubjectsGrade) {
        super(context, R.layout.subject_list_tem,SubjectsName);
        mContext = context;
        mSubjectsName = SubjectsName;
        mSubjectsCode = SubjectsCode;
        mSubjectsGrade = SubjectsGrade;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if(convertView == null){

            convertView = LayoutInflater.from(mContext).inflate(R.layout.subject_list_tem, null);

            holder = new ViewHolder();


            holder.msubjectGrade = (TextView)convertView.findViewById(R.id.subjectGrade);
            holder.mSubjectName = (TextView)convertView.findViewById(R.id.subjectName);
            holder.mSubjectCode = (TextView)convertView.findViewById(R.id.subjectCode);

            convertView.setTag(holder);

        }else{
            holder = (ViewHolder)convertView.getTag();
        }



        holder.mSubjectName.setText(mSubjectsName[position]);
        holder.mSubjectCode.setText(mSubjectsCode[position]);
        holder.msubjectGrade.setText(mSubjectsGrade[position]);


        /**
         *  Animate List View like a card Stack
         *
         *  By Pankaj Adhyapak
         */
        float initialTranslation = (mLastPosition <= position ? 500f : -500f);

        convertView.setTranslationY(initialTranslation);
        convertView.animate()
                .setInterpolator(new DecelerateInterpolator(1.0f))
                .translationY(0f)
                .setDuration(300l)
                .setListener(null);

        // Keep track of the last position we loaded
        mLastPosition = position;

        //return View
        return convertView;
    }

    private static class ViewHolder{
        TextView msubjectGrade;
        TextView mSubjectName;
        TextView mSubjectCode;
    }
}


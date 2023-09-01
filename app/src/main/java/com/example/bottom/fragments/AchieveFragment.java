package com.example.bottom.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bottom.R;
import com.example.bottom.elements.AchieveItem;
import com.example.bottom.models.Apartament;
import com.example.bottom.models.ObjectClass;
import com.example.bottom.viewmodels.ObjectsViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class AchieveFragment extends Fragment {

    long duration = 300;
    PieChart pieChart;
    ProgressBar progressBar;
    LinearLayout holder;
    LinearLayout parentLayout;

    ObjectsViewModel viewModel;
    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_achieve, container, false);



        pieChart = rootView.findViewById(R.id.pieChart);
        progressBar = rootView.findViewById(R.id.achieve_progress_bar);
        parentLayout = rootView.findViewById(R.id.achieve_fragment_holder);
        holder = rootView.findViewById(R.id.achieve_holder);

        return rootView;
    }

    private void FillData(){
        pieChart.getLegend().setEnabled(false);
        pieChart.setDrawCenterText(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.getDescription().setEnabled(false);

        pieChart.setHoleColor(Color.parseColor("#00FFFFFF"));

        pieChart.setHoleRadius(75f);
        pieChart.setRotationAngle(90f);

        // Список значений и меток для круговой диаграммы
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(20f, 0)); // Значение и метка для сектора 0
        entries.add(new PieEntry(10f, 1)); // Значение и метка для сектора 1
        entries.add(new PieEntry(10f, 2)); // Значение и метка для сектора 2


        // Создание набора данных для круговой диаграммы
        PieDataSet dataSet = new PieDataSet(entries,null);
        dataSet.setDrawValues(false);

        // Настройка цветов секторов
        int[] colors = new int[] {Color.GREEN, Color.GRAY, Color.RED};
        dataSet.setColors(colors);
        // Создание объекта данных для круговой диаграммы
        PieData pieData = new PieData(dataSet);
        // Установка данных в PieChart
        pieChart.setData(pieData);
        // Обновление отображения диаграммы
        pieChart.invalidate();

        for (ObjectClass obj:viewModel.getObjectList()) {
            View object = LayoutInflater.from(requireContext()).inflate(R.layout.achieve_item, null);

            LinearLayout builtList = object.findViewById(R.id.achieve_item_build_scroll);
            LinearLayout acceptedList = object.findViewById(R.id.achieve_item_accepted_scroll);
            LinearLayout declinedList = object.findViewById(R.id.achieve_item_decline_scroll);

            LinearLayout titleLayout = object.findViewById(R.id.achieve_item_title_layout);
            ImageView titleArrow = object.findViewById(R.id.achieve_title_arrow);
            TextView titleText = object.findViewById(R.id.achieve_item_title);


            TextView builtText = object.findViewById(R.id.achieve_item_build_text);
            TextView acceptText = object.findViewById(R.id.achieve_item_accepted_text);
            TextView declineText =object.findViewById(R.id.achieve_item_problems_text);

            LinearLayout body = object.findViewById(R.id.achieve_item_body);

            titleText.setText(obj.getName());

            List<Apartament> acceptedApartments = obj.getAcceptedApartments();
            List<Apartament> declinedApartments = obj.getDeclinedApartments();
            List<Apartament> apartments = obj.getApartments();

            builtText.setText(getString(R.string.achieve_built) + " " + apartments.size());
            acceptText.setText(getString(R.string.achieve_accepted) + " " + acceptedApartments.size());
            declineText.setText(getString(R.string.achieve_declined) + " " + declinedApartments.size());

            final int[] transition = new int[1];
            body.post(() -> transition[0] = body.getHeight());
            titleLayout.setOnClickListener(v -> {
                if (body.getVisibility() == View.VISIBLE) {
                    collapseLinearLayout(body, transition[0]);
                    rotateArrow(titleArrow);

                } else if (body.getVisibility() == View.GONE) {
                    expandLinearLayout(body, transition[0]);
                    rotateArrowBack(titleArrow);
                }
            });

            for (Apartament apartment: apartments) {
                AchieveItem item = new AchieveItem(requireContext(),apartment);
                builtList.addView(item);
            }

            for (Apartament apartment: acceptedApartments) {
                AchieveItem item = new AchieveItem(requireContext(),apartment);
                acceptedList.addView(item);
            }

            for (Apartament apartment: declinedApartments) {
                AchieveItem item = new AchieveItem(requireContext(),apartment);
                declinedList.addView(item);
            }

            holder.addView(object);
        }

        progressBar.setVisibility(View.GONE);
        parentLayout.setVisibility(View.VISIBLE);
    }

    private void expandLinearLayout(@NonNull LinearLayout layout, int translationY) {
        layout.setVisibility(View.VISIBLE);

        ValueAnimator animator = ValueAnimator.ofInt(0, translationY);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(duration);
        animator.addUpdateListener(animation -> {
            int animatedValue = (int) animation.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = layout.getLayoutParams();
            layoutParams.height = animatedValue;
            layout.setLayoutParams(layoutParams);
        });
        animator.start();
    }

    private void collapseLinearLayout(LinearLayout layout,int translationY) {
        ValueAnimator animator = ValueAnimator.ofInt(translationY, 0);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(duration);
        animator.addUpdateListener(animation -> {
            int animatedValue = (int) animation.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = layout.getLayoutParams();
            layoutParams.height = animatedValue;
            layout.setLayoutParams(layoutParams);
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                layout.setVisibility(View.GONE);
            }
        });
        animator.start();
    }

    private void rotateArrow(ImageView imageView){
        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(imageView, "rotation", 90f, 0f);
        rotateAnimator.setDuration(duration);
        rotateAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(rotateAnimator);
        animatorSet.start();
    }
    private void rotateArrowBack(ImageView imageView){
        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 90f);
        rotateAnimator.setDuration(duration);
        rotateAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(rotateAnimator);
        animatorSet.start();
    }
}


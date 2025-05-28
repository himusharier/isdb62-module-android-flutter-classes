package com.himusharier.studentcrudapp.util;

import androidx.recyclerview.widget.DiffUtil;

import com.himusharier.studentcrudapp.model.Student;

import java.util.List;
import java.util.Objects;

public class StudentDiffCallback extends DiffUtil.Callback {

    private final List<Student> oldList;
    private final List<Student> newList;

    public StudentDiffCallback(List<Student> oldList, List<Student> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        // Assuming 'id' uniquely identifies an employee
        return Objects.equals(oldList.get(oldItemPosition).getId(), newList.get(newItemPosition).getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}
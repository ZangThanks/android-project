package com.poly.huynhthanhgiang_22716371;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import org.hamcrest.Matcher;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RecyclerViewItemCountAssertion implements ViewAssertion {
    private final Matcher<Integer> matcher;

    public static RecyclerViewItemCountAssertion withItemCount(int expectedCount) {
        return withItemCount(is(expectedCount));
    }

    public static RecyclerViewItemCountAssertion withItemCount(Matcher<Integer> matcher) {
        return new RecyclerViewItemCountAssertion(matcher);
    }

    private RecyclerViewItemCountAssertion(Matcher<Integer> matcher) {
        this.matcher = matcher;
    }

    @Override
    public void check(View view, NoMatchingViewException noViewFoundException) {
        if (noViewFoundException != null) {
            throw noViewFoundException;
        }

        if (!(view instanceof RecyclerView)) {
            throw new IllegalStateException("The asserted view is not a RecyclerView");
        }

        RecyclerView recyclerView = (RecyclerView) view;
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("RecyclerView has no adapter");
        }
        assertThat(adapter.getItemCount(), matcher);
    }
}
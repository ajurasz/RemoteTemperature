package io.github.ajurasz.remotetemp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.github.ajurasz.remotetemp.models.Temperature;
import io.github.ajurasz.remotetemp.rest.RemoteTempApi;
import io.github.ajurasz.remotetemp.rest.RestClient;
import io.github.ajurasz.remotetemp.service.RegistrationIntentService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static io.github.ajurasz.remotetemp.service.DeviceUtil.getDeviceId;

public class RemoteTempWidgetProvider extends AppWidgetProvider {

    private static final String TAG = AppWidgetProvider.class.getSimpleName();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

    public static final String MESSAGE_RECEIVED = "io.github.ajurasz.remotetemp.MESSAGE_RECEIVED";

    @Override
    public void onEnabled(Context context) {
        Intent registrationIntentService = new Intent(context, RegistrationIntentService.class);
        context.startService(registrationIntentService);
    }

    @Override
    public void onDisabled(Context context) {
        Intent registrationIntentService = new Intent(context, RegistrationIntentService.class);
        context.stopService(registrationIntentService);

        RemoteTempApi api = RestClient.getInstance().remoteTempApi();
        api.deleteDevice(getDeviceId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i(TAG, "Device removed");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (MESSAGE_RECEIVED.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            onUpdate(context, appWidgetManager, appWidgetManager.getAppWidgetIds(new ComponentName(context, RemoteTempWidgetProvider.class)));
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        final int count = appWidgetIds.length;

        for (int i = 0; i < count; i++) {
            final int widgetId = appWidgetIds[i];

            RemoteTempApi remoteTempNodeApi = RestClient.getInstance().remoteTempApi();
            remoteTempNodeApi.temperature().enqueue(new Callback<Temperature>() {
                @Override
                public void onResponse(Call<Temperature> call, Response<Temperature> response) {
                    String temperature = String.format(context.getResources().getString(R.string.gauge_value), Float.toString(response.body().getTemperature()));
                    String updatedAt = String.format(context.getResources().getString(R.string.updated_at), sdf.format(new Date()));

                    RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.remotetemp_widget);
                    remoteViews.setTextViewText(R.id.temperatureTextView, temperature);
                    remoteViews.setTextViewText(R.id.updatedAtTextView, updatedAt);
                    remoteViews.setViewVisibility(R.id.updatedAtTextView, View.VISIBLE);

                    Intent intent = new Intent(context, RemoteTempWidgetProvider.class);
                    intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                            0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    remoteViews.setOnClickPendingIntent(R.id.refresh, pendingIntent);

                    appWidgetManager.updateAppWidget(widgetId, remoteViews);
                }

                @Override
                public void onFailure(Call<Temperature> call, Throwable t) {
                    Log.e(TAG, t.getMessage());
                }
            });
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}

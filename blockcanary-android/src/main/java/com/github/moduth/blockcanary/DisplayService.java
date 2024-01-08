/*
 * Copyright (C) 2015 Square, Inc.
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
package com.github.moduth.blockcanary;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.github.moduth.blockcanary.internal.BlockInfo;
import com.github.moduth.blockcanary.ui.DisplayActivity;

final class DisplayService implements BlockInterceptor {

  private static final String TAG = "DisplayService";

  @Override
  public void onBlock(Context context, BlockInfo blockInfo) {
    Intent intent = new Intent(context, DisplayActivity.class);
    intent.putExtra("show_latest", blockInfo.timeStart);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent,
      FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    String contentTitle = context.getString(R.string.block_canary_class_has_blocked, blockInfo.timeStart);
    String contentText = context.getString(R.string.block_canary_notification_message);
    show(context, contentTitle, contentText, pendingIntent);
  }

  private void show(Context context, String contentTitle, String contentText, PendingIntent pendingIntent) {
    NotificationManager notificationManager = (NotificationManager)
      context.getSystemService(Context.NOTIFICATION_SERVICE);

    Notification notification;
    Notification.Builder builder = new Notification.Builder(context)
      .setSmallIcon(R.drawable.block_canary_notification)
      .setChannelId("通知")
      .setWhen(System.currentTimeMillis())
      .setContentTitle(contentTitle)
      .setContentText(contentText)
      .setAutoCancel(true)
      .setContentIntent(pendingIntent)
      .setDefaults(Notification.DEFAULT_SOUND);
    notification = builder.build();
    notificationManager.notify(0xDEAFBEEF, notification);
  }
}

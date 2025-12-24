package com.scoutnerd.app.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.scoutnerd.app.data.local.entity.MetricEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class MetricDao_Impl implements MetricDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MetricEntity> __insertionAdapterOfMetricEntity;

  public MetricDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMetricEntity = new EntityInsertionAdapter<MetricEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `metrics` (`id`,`name`,`type`,`category`,`sortOrder`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final MetricEntity entity) {
        statement.bindLong(1, entity.id);
        if (entity.name == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.name);
        }
        statement.bindLong(3, entity.type);
        statement.bindLong(4, entity.category);
        statement.bindLong(5, entity.sortOrder);
      }
    };
  }

  @Override
  public void insertAll(final List<MetricEntity> metrics) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfMetricEntity.insert(metrics);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insert(final MetricEntity metric) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfMetricEntity.insert(metric);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public LiveData<List<MetricEntity>> getAllMetrics() {
    final String _sql = "SELECT * FROM metrics ORDER BY sortOrder ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"metrics"}, false, new Callable<List<MetricEntity>>() {
      @Override
      @Nullable
      public List<MetricEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final List<MetricEntity> _result = new ArrayList<MetricEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MetricEntity _item;
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final int _tmpType;
            _tmpType = _cursor.getInt(_cursorIndexOfType);
            final int _tmpCategory;
            _tmpCategory = _cursor.getInt(_cursorIndexOfCategory);
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            _item = new MetricEntity(_tmpName,_tmpType,_tmpCategory,_tmpSortOrder);
            _item.id = _cursor.getLong(_cursorIndexOfId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public List<MetricEntity> getAllMetricsSync() {
    final String _sql = "SELECT * FROM metrics ORDER BY sortOrder ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
      final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
      final List<MetricEntity> _result = new ArrayList<MetricEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final MetricEntity _item;
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        final int _tmpType;
        _tmpType = _cursor.getInt(_cursorIndexOfType);
        final int _tmpCategory;
        _tmpCategory = _cursor.getInt(_cursorIndexOfCategory);
        final int _tmpSortOrder;
        _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
        _item = new MetricEntity(_tmpName,_tmpType,_tmpCategory,_tmpSortOrder);
        _item.id = _cursor.getLong(_cursorIndexOfId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int getCount() {
    final String _sql = "SELECT COUNT(*) FROM metrics";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

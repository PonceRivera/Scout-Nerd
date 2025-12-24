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
import com.scoutnerd.app.data.local.entity.MatchResultEntity;
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
public final class MatchResultDao_Impl implements MatchResultDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MatchResultEntity> __insertionAdapterOfMatchResultEntity;

  public MatchResultDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMatchResultEntity = new EntityInsertionAdapter<MatchResultEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `match_results` (`id`,`matchNumber`,`teamNumber`,`eventKey`,`metricId`,`value`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final MatchResultEntity entity) {
        statement.bindLong(1, entity.id);
        statement.bindLong(2, entity.matchNumber);
        statement.bindLong(3, entity.teamNumber);
        if (entity.eventKey == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.eventKey);
        }
        statement.bindLong(5, entity.metricId);
        if (entity.value == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.value);
        }
        statement.bindLong(7, entity.timestamp);
      }
    };
  }

  @Override
  public void insert(final MatchResultEntity result) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfMatchResultEntity.insert(result);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public LiveData<List<MatchResultEntity>> getResultsForTeam(final String eventKey,
      final int teamNumber) {
    final String _sql = "SELECT * FROM match_results WHERE eventKey = ? AND teamNumber = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (eventKey == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, eventKey);
    }
    _argIndex = 2;
    _statement.bindLong(_argIndex, teamNumber);
    return __db.getInvalidationTracker().createLiveData(new String[] {"match_results"}, false, new Callable<List<MatchResultEntity>>() {
      @Override
      @Nullable
      public List<MatchResultEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMatchNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "matchNumber");
          final int _cursorIndexOfTeamNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "teamNumber");
          final int _cursorIndexOfEventKey = CursorUtil.getColumnIndexOrThrow(_cursor, "eventKey");
          final int _cursorIndexOfMetricId = CursorUtil.getColumnIndexOrThrow(_cursor, "metricId");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<MatchResultEntity> _result = new ArrayList<MatchResultEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MatchResultEntity _item;
            final int _tmpMatchNumber;
            _tmpMatchNumber = _cursor.getInt(_cursorIndexOfMatchNumber);
            final int _tmpTeamNumber;
            _tmpTeamNumber = _cursor.getInt(_cursorIndexOfTeamNumber);
            final String _tmpEventKey;
            if (_cursor.isNull(_cursorIndexOfEventKey)) {
              _tmpEventKey = null;
            } else {
              _tmpEventKey = _cursor.getString(_cursorIndexOfEventKey);
            }
            final long _tmpMetricId;
            _tmpMetricId = _cursor.getLong(_cursorIndexOfMetricId);
            final String _tmpValue;
            if (_cursor.isNull(_cursorIndexOfValue)) {
              _tmpValue = null;
            } else {
              _tmpValue = _cursor.getString(_cursorIndexOfValue);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new MatchResultEntity(_tmpMatchNumber,_tmpTeamNumber,_tmpEventKey,_tmpMetricId,_tmpValue,_tmpTimestamp);
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
  public List<MatchResultEntity> getAllResultsForEvent(final String eventKey) {
    final String _sql = "SELECT * FROM match_results WHERE eventKey = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (eventKey == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, eventKey);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfMatchNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "matchNumber");
      final int _cursorIndexOfTeamNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "teamNumber");
      final int _cursorIndexOfEventKey = CursorUtil.getColumnIndexOrThrow(_cursor, "eventKey");
      final int _cursorIndexOfMetricId = CursorUtil.getColumnIndexOrThrow(_cursor, "metricId");
      final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
      final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
      final List<MatchResultEntity> _result = new ArrayList<MatchResultEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final MatchResultEntity _item;
        final int _tmpMatchNumber;
        _tmpMatchNumber = _cursor.getInt(_cursorIndexOfMatchNumber);
        final int _tmpTeamNumber;
        _tmpTeamNumber = _cursor.getInt(_cursorIndexOfTeamNumber);
        final String _tmpEventKey;
        if (_cursor.isNull(_cursorIndexOfEventKey)) {
          _tmpEventKey = null;
        } else {
          _tmpEventKey = _cursor.getString(_cursorIndexOfEventKey);
        }
        final long _tmpMetricId;
        _tmpMetricId = _cursor.getLong(_cursorIndexOfMetricId);
        final String _tmpValue;
        if (_cursor.isNull(_cursorIndexOfValue)) {
          _tmpValue = null;
        } else {
          _tmpValue = _cursor.getString(_cursorIndexOfValue);
        }
        final long _tmpTimestamp;
        _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
        _item = new MatchResultEntity(_tmpMatchNumber,_tmpTeamNumber,_tmpEventKey,_tmpMetricId,_tmpValue,_tmpTimestamp);
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
  public LiveData<List<MatchResultDao.TeamStat>> getAverageMetricStats(final String eventKey,
      final long metricId) {
    final String _sql = "SELECT teamNumber, AVG(CAST(value AS FLOAT)) as avgValue FROM match_results WHERE eventKey = ? AND metricId = ? GROUP BY teamNumber ORDER BY avgValue DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (eventKey == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, eventKey);
    }
    _argIndex = 2;
    _statement.bindLong(_argIndex, metricId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"match_results"}, false, new Callable<List<MatchResultDao.TeamStat>>() {
      @Override
      @Nullable
      public List<MatchResultDao.TeamStat> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfTeamNumber = 0;
          final int _cursorIndexOfAvgValue = 1;
          final List<MatchResultDao.TeamStat> _result = new ArrayList<MatchResultDao.TeamStat>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MatchResultDao.TeamStat _item;
            _item = new MatchResultDao.TeamStat();
            _item.teamNumber = _cursor.getInt(_cursorIndexOfTeamNumber);
            _item.avgValue = _cursor.getFloat(_cursorIndexOfAvgValue);
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
  public LiveData<List<MatchResultDao.TeamMatchCount>> getTeamMatchCounts(final String eventKey) {
    final String _sql = "SELECT teamNumber, COUNT(DISTINCT matchNumber) as matchCount FROM match_results WHERE eventKey = ? GROUP BY teamNumber ORDER BY matchCount DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (eventKey == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, eventKey);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"match_results"}, false, new Callable<List<MatchResultDao.TeamMatchCount>>() {
      @Override
      @Nullable
      public List<MatchResultDao.TeamMatchCount> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfTeamNumber = 0;
          final int _cursorIndexOfMatchCount = 1;
          final List<MatchResultDao.TeamMatchCount> _result = new ArrayList<MatchResultDao.TeamMatchCount>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MatchResultDao.TeamMatchCount _item;
            _item = new MatchResultDao.TeamMatchCount();
            _item.teamNumber = _cursor.getInt(_cursorIndexOfTeamNumber);
            _item.matchCount = _cursor.getInt(_cursorIndexOfMatchCount);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

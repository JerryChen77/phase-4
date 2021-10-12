package com.boot.security.server.service;

import com.boot.security.server.model.SysLogs;

/**
 * 日志service
 */
public interface SysLogService {

	void save(SysLogs sysLogs);

	void save(Long userId, String module, Boolean flag, String remark);

	void deleteLogs();
}

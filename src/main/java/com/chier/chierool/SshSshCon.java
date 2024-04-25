package com.chier.chierool;

/**
 * @author lmdm
 * protive the SSH SSL conntion
 */
public class SshSshCon {

    public void conntionJsch(){
//        JSch jsch = new JSch();
//
//        // 建立SSH连接
//        Session session = jsch.getSession(user, host, port);
//        session.setPassword(password);
//        session.setConfig("StrictHostKeyChecking", "no"); // 设置连接时不进行HostKey检查
//        session.connect();
//
//        // 执行命令
//        String command = "your_command";
//        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
//        channelExec.setCommand(command);
//        channelExec.setInputStream(null);
//        channelExec.setErrStream(System.err);
//
//        // 获取命令输出
//        channelExec.connect();
//        InputStream in = channelExec.getInputStream();
//        byte[] tmp = new byte[1024];
//        while (true) {
//            while (in.available() > 0) {
//                int i = in.read(tmp, 0, 1024);
//                if (i < 0) break;
//                System.out.print(new String(tmp, 0, i));
//            }
//            if (channelExec.isClosed()) {
//                if (in.available() > 0) continue;
//                System.out.println("exit-status: " + channelExec.getExitStatus());
//                break;
//            }
//            try {
//                Thread.sleep(1000);
//            } catch (Exception ee) {
//            }
//        }
//
//        // 关闭连接
//        channelExec.disconnect();
//        session.disconnect();
    }
}

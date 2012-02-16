/*     */ package net.minecraft;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Frame;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.URLEncoder;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.imageio.ImageIO;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JPasswordField;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.UIManager;
/*     */ 
/*     */ public class LauncherFrame extends Frame
/*     */ {
/*     */   public static final int VERSION = 13;
/*     */   private static final long serialVersionUID = 1L;
/*  21 */   public Map<String, String> customParameters = new HashMap();
/*     */   public Launcher launcher;
/*     */   public LoginForm loginForm;
/*     */ 
/*     */   public LauncherFrame()
/*     */   {
/*  27 */     super("FarmCraft");
/*     */ 
/*  29 */     setBackground(Color.BLACK);
/*  30 */     this.loginForm = new LoginForm(this);
/*  31 */     JPanel p = new JPanel();
/*  32 */     p.setLayout(new BorderLayout());
/*  33 */     p.add(this.loginForm, "Center");
/*     */ 
/*  35 */     p.setPreferredSize(new Dimension(854, 480));
/*     */ 
/*  37 */     setLayout(new BorderLayout());
/*  38 */     add(p, "Center");
/*     */ 
/*  40 */     pack();
/*  41 */     setLocationRelativeTo(null);
/*     */     try
/*     */     {
/*  44 */       setIconImage(ImageIO.read(LauncherFrame.class.getResource("favicon.png")));
/*     */     } catch (IOException e1) {
/*  46 */       e1.printStackTrace();
/*     */     }
/*     */ 
/*  49 */     addWindowListener(new WindowAdapter() {
/*     */       public void windowClosing(WindowEvent arg0) {
/*  51 */         new Thread() {
/*     */           public void run() {
/*     */             try {
/*  54 */               Thread.sleep(30000L);
/*     */             } catch (InterruptedException e) {
/*  56 */               e.printStackTrace();
/*     */             }
/*  58 */             System.out.println("FORCING EXIT!");
/*  59 */             System.exit(0);
/*     */           }
/*     */         }
/*  62 */         .start();
/*  63 */         if (LauncherFrame.this.launcher != null) {
/*  64 */           LauncherFrame.this.launcher.stop();
/*  65 */           LauncherFrame.this.launcher.destroy();
/*     */         }
/*  67 */         System.exit(0);
/*     */       } } );
/*     */   }
/*     */ 
/*     */   public void playCached(String userName) {
/*     */     try {
/*  74 */       if ((userName == null) || (userName.length() <= 0)) {
/*  75 */         userName = "Player";
/*     */       }
/*  77 */       this.launcher = new Launcher();
/*  78 */       this.launcher.customParameters.putAll(this.customParameters);
/*  79 */       this.launcher.customParameters.put("userName", userName);
/*  80 */       this.launcher.init();
/*  81 */       removeAll();
/*  82 */       add(this.launcher, "Center");
/*  83 */       validate();
/*  84 */       this.launcher.start();
/*  85 */       this.loginForm = null;
/*  86 */       setTitle("FarmCraft");
/*     */     } catch (Exception e) {
/*  88 */       e.printStackTrace();
/*  89 */       showError(e.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void login(String userName, String password) {
/*     */     try {
/*  95 */       String parameters = "user=" + URLEncoder.encode(userName, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8") + "&version=" + 13;
/*  96 */       String result = Util.excutePost("https://login.minecraft.net/", parameters);
/*  97 */       if (result == null) {
/*  98 */         showError("Impossible de se connecter à minecraft.net");
/*  99 */         this.loginForm.setNoNetwork();
/* 100 */         return;
/*     */       }
/* 102 */       if (!result.contains(":")) {
/* 103 */         if (result.trim().equals("Mauvais mot de passe")) {
/* 104 */           showError("Echec de l'identification");
/* 105 */         } else if (result.trim().equals("Trop vieille version du Launcher")) {
/* 106 */           this.loginForm.setOutdated();
/* 107 */           showError("Trop vieille version du Launcher");
/*     */         } else {
/* 109 */           showError(result);
/*     */         }
/* 111 */         this.loginForm.setNoNetwork();
/* 112 */         return;
/*     */       }
/* 114 */       String[] values = result.split(":");
/*     */ 
/* 116 */       this.launcher = new Launcher();
/* 117 */       this.launcher.customParameters.putAll(this.customParameters);
/* 118 */       this.launcher.customParameters.put("userName", values[2].trim());
/* 119 */       this.launcher.customParameters.put("latestVersion", values[0].trim());
/* 120 */       this.launcher.customParameters.put("downloadTicket", values[1].trim());
/* 121 */       this.launcher.customParameters.put("sessionId", values[3].trim());
/* 122 */       this.launcher.init();
/*     */ 
/* 124 */       removeAll();
/* 125 */       add(this.launcher, "Center");
/* 126 */       validate();
/* 127 */       this.launcher.start();
/* 128 */       this.loginForm.loginOk();
/* 129 */       this.loginForm = null;
/* 130 */       setTitle("FarmCraft");
/*     */     } catch (Exception e) {
/* 132 */       e.printStackTrace();
/* 133 */       showError(e.toString());
/* 134 */       this.loginForm.setNoNetwork();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void showError(String error) {
/* 139 */     removeAll();
/* 140 */     add(this.loginForm);
/* 141 */     this.loginForm.setError(error);
/* 142 */     validate();
/*     */   }
/*     */ 
/*     */   public boolean canPlayOffline(String userName) {
/* 146 */     Launcher launcher = new Launcher();
/* 147 */     launcher.customParameters.putAll(this.customParameters);
/* 148 */     launcher.init(userName, null, null, null);
/* 149 */     return launcher.canPlayOffline();
/*     */   }
/*     */ 
/*     */   public static void main(String[] args) {
/*     */     try {
/* 154 */       UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/* 158 */     LauncherFrame launcherFrame = new LauncherFrame();
/* 159 */     launcherFrame.setVisible(true);
/* 160 */     launcherFrame.customParameters.put("stand-alone", "true");
/*     */ 
/* 162 */     if (args.length >= 3) {
/* 163 */       String ip = args[2];
/* 164 */       String port = "25565";
/* 165 */       if (ip.contains(":")) {
/* 166 */         String[] parts = ip.split(":");
/* 167 */         ip = parts[0];
/* 168 */         port = parts[1];
/*     */       }
/*     */ 
/* 171 */       launcherFrame.customParameters.put("server", ip);
/* 172 */       launcherFrame.customParameters.put("port", port);
/*     */     }
/*     */ 
/* 175 */     if (args.length >= 1) {
/* 176 */       launcherFrame.loginForm.userName.setText(args[0]);
/* 177 */       if (args.length >= 2) {
/* 178 */         launcherFrame.loginForm.password.setText(args[1]);
/* 179 */         launcherFrame.loginForm.doLogin();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Documents and Settings\FLEJA\Bureau\minecraft.jar
 * Qualified Name:     net.minecraft.LauncherFrame
 * JD-Core Version:    0.6.0
 */
// screens-early.jsx — Splash, Onboarding, Permissions, Home

function SplashScreen({ onDone }) {
  React.useEffect(() => {
    const t = setTimeout(onDone, 1800);
    return () => clearTimeout(t);
  }, []);
  return (
    <div className="screen" style={{ background: 'var(--bg-primary)' }}>
      <div className="paper-grain" />
      <StatusBar />
      <div style={{
        flex: 1, display: 'flex', flexDirection: 'column',
        alignItems: 'center', justifyContent: 'center', gap: 28,
      }}>
        <div style={{ animation: 'fadeIn 800ms ease-out both' }}>
          <svg width="72" height="84" viewBox="0 0 72 84" fill="none">
            <path d="M8 4h44l16 16v56a4 4 0 0 1-4 4H8a4 4 0 0 1-4-4V8a4 4 0 0 1 4-4z"
              stroke="var(--accent)" strokeWidth="2"/>
            <path d="M52 4v16h16" stroke="var(--accent)" strokeWidth="2" strokeLinejoin="round"/>
            <path d="M16 38h32M16 48h32M16 58h22" stroke="var(--accent)" strokeWidth="1.5" strokeLinecap="round" opacity="0.55"/>
          </svg>
        </div>
        <div style={{
          fontFamily: 'var(--display)', fontSize: 34, fontWeight: 600,
          color: 'var(--text-primary)', letterSpacing: '0.02em',
          animation: 'fadeIn 900ms 200ms ease-out both',
        }}>Архив</div>
        <div style={{
          fontFamily: 'var(--mono)', fontSize: 11,
          color: 'var(--text-tertiary)', letterSpacing: '0.15em',
          textTransform: 'uppercase',
          animation: 'fadeIn 900ms 400ms ease-out both',
        }}>document · scanner</div>
      </div>
    </div>
  );
}

function OnboardingScreen({ onDone }) {
  const [step, setStep] = React.useState(0);
  const slides = [
    {
      illo: (
        <svg width="200" height="180" viewBox="0 0 200 180" fill="none">
          {/* phone */}
          <rect x="58" y="20" width="80" height="140" rx="10" stroke="var(--accent)" strokeWidth="1.5"/>
          <rect x="68" y="34" width="60" height="110" rx="2" stroke="var(--accent-light)" strokeWidth="1" opacity="0.5"/>
          {/* document behind */}
          <path d="M22 52 L170 26 L176 140 L28 166 Z"
            stroke="var(--accent)" strokeWidth="1.5" fill="var(--bg-primary)" opacity="0.7"
            transform="rotate(-4 100 90)"/>
          <path d="M42 68 L152 50 M44 80 L140 64 M46 92 L148 76 M46 104 L130 90"
            stroke="var(--accent-light)" strokeWidth="1" opacity="0.8"/>
          {/* scanner lens */}
          <circle cx="98" cy="80" r="14" stroke="var(--accent)" strokeWidth="1.5" fill="var(--bg-primary)"/>
          <circle cx="98" cy="80" r="7" stroke="var(--accent)" strokeWidth="1"/>
          {/* corners */}
          <path d="M78 58 L78 64 M78 58 L84 58" stroke="var(--accent)" strokeWidth="2" strokeLinecap="round"/>
          <path d="M118 58 L118 64 M118 58 L112 58" stroke="var(--accent)" strokeWidth="2" strokeLinecap="round"/>
          <path d="M78 102 L78 96 M78 102 L84 102" stroke="var(--accent)" strokeWidth="2" strokeLinecap="round"/>
          <path d="M118 102 L118 96 M118 102 L112 102" stroke="var(--accent)" strokeWidth="2" strokeLinecap="round"/>
        </svg>
      ),
      title: "Сканируйте\nлюбые документы",
      sub: "Автоматическое определение границ и коррекция перспективы — в реальном времени."
    },
    {
      illo: (
        <svg width="200" height="180" viewBox="0 0 200 180" fill="none">
          <rect x="36" y="20" width="128" height="140" rx="4" stroke="var(--accent)" strokeWidth="1.5" fill="var(--bg-primary)"/>
          {[0,1,2,3,4,5,6].map(i => (
            <g key={i}>
              <rect x="50" y={38 + i*16} width={[70,90,58,82,64,76,50][i]} height="4" rx="1"
                fill={i===2 || i===4 ? "var(--accent)" : "var(--bg-tertiary)"} opacity={i===2||i===4?0.9:0.6}/>
            </g>
          ))}
          <rect x="48" y="68" width="62" height="8" fill="var(--accent-subtle)" opacity="0.9"/>
          <rect x="48" y="100" width="68" height="8" fill="var(--accent-subtle)" opacity="0.9"/>
          <text x="100" y="176" textAnchor="middle" fontFamily="DM Mono" fontSize="9" fill="var(--text-tertiary)" letterSpacing="0.1em">OCR · ABC · РУС</text>
        </svg>
      ),
      title: "Распознавание\nтекста",
      sub: "Печатный текст, латиница и кириллица — полностью офлайн на устройстве."
    },
    {
      illo: (
        <svg width="200" height="180" viewBox="0 0 200 180" fill="none">
          <path d="M16 48a4 4 0 0 1 4-4h38l10 10h112a4 4 0 0 1 4 4v90a4 4 0 0 1-4 4H20a4 4 0 0 1-4-4z"
            stroke="var(--accent)" strokeWidth="1.5" fill="var(--bg-primary)"/>
          <rect x="36" y="64" width="48" height="62" rx="2" fill="var(--surface-raised)" stroke="var(--bg-tertiary)"/>
          <rect x="92" y="64" width="48" height="62" rx="2" fill="var(--surface-raised)" stroke="var(--bg-tertiary)" transform="rotate(3 116 95)"/>
          <rect x="148" y="64" width="36" height="62" rx="2" fill="var(--surface-raised)" stroke="var(--bg-tertiary)" transform="rotate(-2 166 95)"/>
          {[0,1,2].map(i => (
            <g key={i} transform={`translate(${36 + i*56}, 76)`}>
              <rect width="28" height="2" rx="1" fill="var(--accent-light)" opacity="0.6"/>
              <rect y="8" width="22" height="2" rx="1" fill="var(--accent-light)" opacity="0.45"/>
              <rect y="16" width="26" height="2" rx="1" fill="var(--accent-light)" opacity="0.45"/>
            </g>
          ))}
        </svg>
      ),
      title: "Храните\nи делитесь",
      sub: "PDF, JPG или PNG — одним касанием. Всё локально, без облака."
    }
  ];
  const s = slides[step];

  const next = () => {
    if (step < 2) setStep(step + 1);
    else onDone();
  };

  return (
    <div className="screen" style={{ background: 'var(--bg-primary)' }}>
      <div className="paper-grain" />
      <StatusBar />
      {/* Skip */}
      <button onClick={onDone} style={{
        position: 'absolute', top: 60, right: 24, zIndex: 10,
        background: 'transparent', border: 'none', cursor: 'pointer',
        fontFamily: 'var(--body)', fontSize: 14, color: 'var(--text-tertiary)',
      }}>Пропустить</button>

      <div style={{
        flex: 1, display: 'flex', flexDirection: 'column',
        padding: '100px 32px 40px', position: 'relative',
      }}>
        <div key={step} style={{
          flex: 1, display: 'flex', flexDirection: 'column',
          alignItems: 'center', justifyContent: 'center',
          animation: 'fadeIn 400ms ease-out both',
        }}>
          <div style={{ marginBottom: 48 }}>{s.illo}</div>
          <h1 style={{
            fontFamily: 'var(--display)', fontSize: 36, fontWeight: 600,
            lineHeight: 1.1, letterSpacing: '0.01em',
            color: 'var(--text-primary)', margin: 0,
            textAlign: 'center', whiteSpace: 'pre-line',
          }}>{s.title}</h1>
          <p style={{
            fontFamily: 'var(--body)', fontWeight: 300, fontSize: 16,
            lineHeight: 1.5, color: 'var(--text-secondary)',
            margin: '20px 0 0', textAlign: 'center', maxWidth: 280,
          }}>{s.sub}</p>
        </div>

        {/* dots */}
        <div style={{ display: 'flex', justifyContent: 'center', gap: 8, marginBottom: 24 }}>
          {[0,1,2].map(i => (
            <div key={i} style={{
              width: i === step ? 24 : 6, height: 6, borderRadius: 3,
              background: i === step ? 'var(--accent)' : 'var(--bg-tertiary)',
              transition: 'all 300ms ease',
            }} />
          ))}
        </div>
        <button className="btn btn-primary" onClick={next} style={{ width: '100%' }}>
          {step === 2 ? 'Начать' : 'Далее'}
        </button>
      </div>
      <HomeIndicator />
    </div>
  );
}

function PermissionsScreen({ onDone }) {
  const [stage, setStage] = React.useState(0);
  const perms = [
    { icon: IconCamera, title: "Доступ к камере",
      desc: "Нужен для сканирования документов. Фото снимаются и обрабатываются локально — ничего не уходит в сеть." },
    { icon: IconImage, title: "Доступ к галерее",
      desc: "Чтобы сканировать фото, уже сохранённые на устройстве. Вы выбираете, какие файлы открыть." },
    { icon: IconFolder, title: "Доступ к файлам",
      desc: "Нужен для экспорта готовых PDF и изображений в другие приложения." },
  ];
  const [showDialog, setShowDialog] = React.useState(false);

  const p = perms[stage];
  const Icn = p.icon;

  return (
    <div className="screen" style={{ background: 'var(--bg-primary)' }}>
      <div className="paper-grain" />
      <StatusBar />
      <div style={{
        flex: 1, padding: '80px 32px 40px',
        display: 'flex', flexDirection: 'column',
      }}>
        <div style={{
          fontFamily: 'var(--mono)', fontSize: 11, color: 'var(--text-tertiary)',
          letterSpacing: '0.15em', textTransform: 'uppercase', marginBottom: 32,
        }}>Шаг {stage + 1} из 3</div>

        <div key={stage} style={{ animation: 'fadeIn 350ms ease-out both' }}>
          <div style={{
            width: 88, height: 88, borderRadius: 22,
            background: 'var(--accent-subtle)',
            display: 'flex', alignItems: 'center', justifyContent: 'center',
            marginBottom: 32,
          }}>
            <Icn size={38} stroke="var(--accent)" strokeWidth={1.6} />
          </div>
          <h1 style={{
            fontFamily: 'var(--display)', fontSize: 30, fontWeight: 600,
            margin: 0, color: 'var(--text-primary)', letterSpacing: '0.01em',
          }}>{p.title}</h1>
          <p style={{
            fontFamily: 'var(--body)', fontSize: 16, lineHeight: 1.55,
            color: 'var(--text-secondary)', marginTop: 16, fontWeight: 300,
          }}>{p.desc}</p>
        </div>

        <div style={{ flex: 1 }} />

        <button className="btn btn-primary" onClick={() => setShowDialog(true)} style={{ width: '100%' }}>
          Разрешить
        </button>
        <button className="btn btn-ghost" onClick={() => {
          if (stage < 2) setStage(stage + 1); else onDone();
        }} style={{ width: '100%', marginTop: 8 }}>
          Не сейчас
        </button>
      </div>
      <HomeIndicator />

      {showDialog && (
        <>
          <div className="sheet-backdrop" onClick={() => setShowDialog(false)} />
          <div style={{
            position: 'absolute', inset: 0, zIndex: 160,
            display: 'flex', alignItems: 'center', justifyContent: 'center',
            padding: 32, pointerEvents: 'none',
          }}>
            <div style={{
              background: 'var(--surface)',
              borderRadius: 14, padding: '24px 20px 8px',
              width: '100%', pointerEvents: 'auto',
              boxShadow: 'var(--shadow-lg)',
              animation: 'fadeIn 200ms ease-out both',
            }}>
              <div style={{
                fontFamily: 'var(--body)', fontSize: 17, fontWeight: 600,
                textAlign: 'center', marginBottom: 8,
              }}>Разрешить «Архиву» доступ к {p.title.toLowerCase().replace('доступ к ','')}?</div>
              <div style={{
                fontFamily: 'var(--body)', fontSize: 13, fontWeight: 300,
                color: 'var(--text-secondary)', textAlign: 'center',
                lineHeight: 1.4, marginBottom: 16,
              }}>Приложение использует данные только локально.</div>
              <div style={{ display: 'flex', borderTop: '1px solid var(--bg-tertiary)', margin: '0 -20px' }}>
                <button onClick={() => setShowDialog(false)} style={{
                  flex: 1, padding: '14px', background: 'transparent', border: 0,
                  borderRight: '1px solid var(--bg-tertiary)',
                  fontFamily: 'var(--body)', fontSize: 15, color: 'var(--text-secondary)',
                  cursor: 'pointer',
                }}>Запретить</button>
                <button onClick={() => {
                  setShowDialog(false);
                  setTimeout(() => {
                    if (stage < 2) setStage(stage + 1); else onDone();
                  }, 150);
                }} style={{
                  flex: 1, padding: '14px', background: 'transparent', border: 0,
                  fontFamily: 'var(--body)', fontSize: 15, fontWeight: 600,
                  color: 'var(--accent)', cursor: 'pointer',
                }}>Разрешить</button>
              </div>
            </div>
          </div>
        </>
      )}
    </div>
  );
}

Object.assign(window, { SplashScreen, OnboardingScreen, PermissionsScreen });

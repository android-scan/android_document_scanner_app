// screens-camera.jsx — Camera, Crop, Edit, Save

function CameraScreen({ onCapture, onClose, onGallery }) {
  const [detected, setDetected] = React.useState(false);
  const [flash, setFlash] = React.useState(false);
  const [captureAnim, setCaptureAnim] = React.useState(false);

  React.useEffect(() => {
    const t1 = setTimeout(() => setDetected(true), 1400);
    return () => clearTimeout(t1);
  }, []);

  const doCapture = () => {
    setCaptureAnim(true);
    setTimeout(() => onCapture(), 320);
  };

  return (
    <div className="screen" style={{ background: 'var(--camera-bg)' }}>
      <StatusBar dark />

      {/* Camera preview — dark with paper rectangle */}
      <div style={{ position: 'absolute', inset: 0, background: 'var(--camera-bg)' }}>
        {/* simulated document — subtly visible paper in the center */}
        <div style={{
          position: 'absolute',
          left: '50%', top: '50%',
          transform: 'translate(-50%, -50%) rotate(-2.5deg)',
          width: 260, height: 340,
          background: 'linear-gradient(135deg, #F2EDE3 0%, #EAE2D2 100%)',
          boxShadow: '0 20px 50px rgba(0,0,0,0.5)',
          opacity: 0.92,
        }}>
          <div style={{ padding: '40px 28px' }}>
            {[...Array(12)].map((_,i) => (
              <div key={i} style={{
                height: 3, marginBottom: 11,
                width: `${60 + (i*7 % 35)}%`,
                background: 'rgba(28,25,23,0.22)',
                borderRadius: 1,
              }}/>
            ))}
          </div>
        </div>

        {/* Scanner sweep line */}
        {detected && (
          <div style={{
            position: 'absolute',
            left: 'calc(50% - 135px)', top: 'calc(50% - 175px)',
            width: 270, height: 350,
            overflow: 'hidden',
          }}>
            <div style={{
              position: 'absolute', left: 0, right: 0, height: 40,
              background: 'linear-gradient(to bottom, transparent, rgba(196,168,130,0.35), transparent)',
              animation: 'scannerSweep 2.5s ease-in-out infinite',
            }}/>
          </div>
        )}

        {/* L-shaped corner markers */}
        <CameraOverlay detected={detected} />

        {/* capture flash */}
        {captureAnim && (
          <div style={{ position: 'absolute', inset: 0, background: '#fff',
            animation: 'flash 320ms ease-out both', zIndex: 80 }}/>
        )}
      </div>

      {/* Top bar */}
      <div style={{
        position: 'absolute', top: 0, left: 0, right: 0, paddingTop: 54,
        zIndex: 20,
        background: 'linear-gradient(to bottom, rgba(15,13,11,0.75), transparent)',
      }}>
        <div style={{
          padding: '16px 20px', display: 'flex',
          alignItems: 'center', justifyContent: 'space-between',
        }}>
          <button onClick={onClose} style={{
            width: 40, height: 40, borderRadius: 20,
            background: 'rgba(15,13,11,0.5)',
            backdropFilter: 'blur(12px)',
            border: '1px solid rgba(255,255,255,0.1)',
            display: 'flex', alignItems: 'center', justifyContent: 'center',
            color: '#fff', cursor: 'pointer',
          }}><IconX size={20}/></button>
          <div style={{
            fontFamily: 'var(--display)', fontSize: 17, fontWeight: 500,
            color: '#fff', letterSpacing: '0.02em',
          }}>Сканирование</div>
          <button onClick={() => setFlash(!flash)} style={{
            width: 40, height: 40, borderRadius: 20,
            background: flash ? 'var(--accent)' : 'rgba(15,13,11,0.5)',
            backdropFilter: 'blur(12px)',
            border: '1px solid rgba(255,255,255,0.1)',
            display: 'flex', alignItems: 'center', justifyContent: 'center',
            color: '#fff', cursor: 'pointer',
          }}>{flash ? <IconFlash size={18}/> : <IconFlashOff size={18}/>}</button>
        </div>
      </div>

      {/* Status pill */}
      <div style={{
        position: 'absolute', top: 140, left: '50%',
        transform: 'translateX(-50%)', zIndex: 20,
      }}>
        <div style={{
          background: 'rgba(15,13,11,0.65)', backdropFilter: 'blur(12px)',
          padding: '8px 14px', borderRadius: 20,
          fontFamily: 'var(--body)', fontSize: 13, fontWeight: 500,
          color: '#fff', display: 'flex', alignItems: 'center', gap: 8,
          border: '1px solid rgba(255,255,255,0.08)',
        }}>
          <span style={{
            width: 7, height: 7, borderRadius: '50%',
            background: detected ? 'var(--accent-light)' : '#fff',
            animation: detected ? 'none' : 'pulse 1.5s ease-in-out infinite',
          }}/>
          {detected ? 'Документ обнаружен — держите ровно' : 'Наводим на документ…'}
        </div>
      </div>

      {/* Bottom bar */}
      <div style={{
        position: 'absolute', bottom: 0, left: 0, right: 0,
        paddingBottom: 36, paddingTop: 20,
        background: 'linear-gradient(to top, rgba(15,13,11,0.85), transparent)',
        zIndex: 20,
      }}>
        <div style={{
          display: 'flex', alignItems: 'center',
          justifyContent: 'space-between', padding: '0 32px',
        }}>
          <button onClick={onGallery} style={{
            width: 48, height: 48, borderRadius: 12,
            background: 'rgba(255,255,255,0.1)',
            backdropFilter: 'blur(12px)',
            border: '1px solid rgba(255,255,255,0.15)',
            display: 'flex', alignItems: 'center', justifyContent: 'center',
            color: '#fff', cursor: 'pointer',
          }}><IconImage size={22} strokeWidth={1.6}/></button>

          {/* Shutter */}
          <button onClick={doCapture} style={{
            width: 76, height: 76, borderRadius: '50%',
            background: 'transparent', border: '4px solid var(--accent-light)',
            padding: 0, cursor: 'pointer', position: 'relative',
            transition: 'transform 120ms',
          }} onMouseDown={e => e.currentTarget.style.transform='scale(0.92)'}
             onMouseUp={e => e.currentTarget.style.transform='scale(1)'}
             onMouseLeave={e => e.currentTarget.style.transform='scale(1)'}>
            <div style={{
              position: 'absolute', inset: 6,
              borderRadius: '50%', background: '#fff',
            }}/>
          </button>

          <div style={{
            width: 48, fontFamily: 'var(--mono)', fontSize: 11,
            color: 'rgba(255,255,255,0.8)', textAlign: 'center',
            letterSpacing: '0.08em',
          }}>СТР. 1</div>
        </div>
      </div>

      <HomeIndicator light />
    </div>
  );
}

function CameraOverlay({ detected }) {
  const color = detected ? 'var(--accent-light)' : '#fff';
  const size = 260, height = 340;
  const armLen = 28;
  const stroke = 2.5;
  // corners
  const Corner = ({ style, d }) => (
    <svg width={armLen+4} height={armLen+4} style={{ position: 'absolute', ...style }}>
      <path d={d} stroke={color} strokeWidth={stroke} strokeLinecap="round" fill="none"
        strokeDasharray={detected ? '0' : '4 4'}
        style={{ filter: detected ? `drop-shadow(0 0 6px ${color})` : 'none',
          transition: 'all 200ms' }}/>
    </svg>
  );
  const cx = 195 - size/2; // phone width 390/2 - box/2
  const cy = 422 - height/2;
  return (
    <div style={{ position: 'absolute', inset: 0, pointerEvents: 'none', zIndex: 10 }}>
      {/* Corner markers */}
      <Corner style={{ left: `calc(50% - ${size/2 + 2}px)`, top: `calc(50% - ${height/2 + 2}px)` }}
        d={`M2 ${armLen} L2 2 L${armLen} 2`}/>
      <Corner style={{ right: `calc(50% - ${size/2 + 2}px)`, top: `calc(50% - ${height/2 + 2}px)` }}
        d={`M${armLen+2} ${armLen} L${armLen+2} 2 L2 2`}/>
      <Corner style={{ left: `calc(50% - ${size/2 + 2}px)`, bottom: `calc(50% - ${height/2 + 2}px)` }}
        d={`M2 2 L2 ${armLen+2} L${armLen} ${armLen+2}`}/>
      <Corner style={{ right: `calc(50% - ${size/2 + 2}px)`, bottom: `calc(50% - ${height/2 + 2}px)` }}
        d={`M${armLen+2} 2 L${armLen+2} ${armLen+2} L2 ${armLen+2}`}/>

      {/* Connecting rectangle when detected */}
      {detected && (
        <div style={{
          position: 'absolute',
          left: `calc(50% - ${size/2}px)`, top: `calc(50% - ${height/2}px)`,
          width: size, height: height,
          border: `1.5px solid ${color}`,
          opacity: 0.65,
          transition: 'opacity 300ms',
        }}/>
      )}
    </div>
  );
}

function CropScreen({ onApply, onRetake }) {
  return (
    <div className="screen" style={{ background: '#14110E' }}>
      <StatusBar dark />
      {/* Document photo */}
      <div style={{ position: 'absolute', inset: 0 }}>
        <div style={{
          position: 'absolute', left: 50, top: 120, right: 30, bottom: 200,
          background: 'linear-gradient(135deg, #F2EDE3 0%, #D9CFBC 100%)',
          transform: 'perspective(600px) rotateX(4deg) rotateY(-3deg)',
        }}>
          <div style={{ padding: '36px 32px' }}>
            <div style={{ fontFamily: 'var(--display)', fontSize: 18, fontWeight: 600,
              color: 'rgba(28,25,23,0.85)', marginBottom: 16 }}>Договор №47/2026</div>
            {[...Array(14)].map((_,i) => (
              <div key={i} style={{
                height: 2.5, marginBottom: 9,
                width: `${55 + (i*13 % 40)}%`,
                background: 'rgba(28,25,23,0.35)', borderRadius: 1,
              }}/>
            ))}
          </div>
        </div>
      </div>

      {/* Crop handles overlay */}
      <CropHandles />

      {/* Back arrow */}
      <button style={{
        position: 'absolute', top: 60, left: 16, zIndex: 30,
        width: 40, height: 40, borderRadius: 20,
        background: 'rgba(15,13,11,0.55)', backdropFilter: 'blur(12px)',
        border: 0, color: '#fff', cursor: 'pointer',
        display: 'flex', alignItems: 'center', justifyContent: 'center',
      }} onClick={onRetake}><IconArrowLeft size={20}/></button>

      <div style={{
        position: 'absolute', top: 60, right: 16, zIndex: 30,
        padding: '10px 14px',
        background: 'rgba(15,13,11,0.55)', backdropFilter: 'blur(12px)',
        borderRadius: 20,
        fontFamily: 'var(--body)', fontSize: 12, fontWeight: 500,
        color: '#fff', letterSpacing: '0.05em',
      }}>Настройте границы</div>

      {/* Bottom actions */}
      <div style={{
        position: 'absolute', bottom: 0, left: 0, right: 0,
        padding: '24px 20px 40px',
        background: 'linear-gradient(to top, rgba(15,13,11,0.95) 60%, transparent)',
        display: 'flex', gap: 12, zIndex: 30,
      }}>
        <button className="btn btn-secondary" onClick={onRetake}
          style={{ flex: 1, background: 'rgba(255,255,255,0.05)' }}>Переснять</button>
        <button className="btn btn-primary" onClick={onApply} style={{ flex: 1 }}>Применить</button>
      </div>
      <HomeIndicator light/>
    </div>
  );
}

function CropHandles() {
  // 4 corner circles, gold
  const corners = [
    { left: '10%', top: '16%' },
    { right: '9%', top: '14%' },
    { left: '12%', bottom: '24%' },
    { right: '7%', bottom: '25%' },
  ];
  return (
    <div style={{ position: 'absolute', inset: 0, pointerEvents: 'none', zIndex: 20 }}>
      {/* connecting lines using SVG */}
      <svg style={{ position: 'absolute', inset: 0, width: '100%', height: '100%' }}>
        <polygon
          points="39,135 355,118 342,634 47,644"
          fill="rgba(196,168,130,0.08)"
          stroke="var(--accent)" strokeWidth="1.5" strokeDasharray="4 3"/>
      </svg>
      {corners.map((c, i) => (
        <div key={i} style={{
          position: 'absolute', ...c,
          width: 22, height: 22, borderRadius: '50%',
          background: 'var(--accent)', border: '3px solid #fff',
          boxShadow: '0 2px 6px rgba(0,0,0,0.4)',
        }}/>
      ))}
      {/* magnifier on top-right corner */}
      <div style={{
        position: 'absolute', right: '9%', top: 72,
        width: 84, height: 84, borderRadius: '50%',
        border: '3px solid var(--accent)',
        background: 'linear-gradient(135deg, #F2EDE3 0%, #D9CFBC 100%)',
        boxShadow: '0 4px 16px rgba(0,0,0,0.5)',
        overflow: 'hidden',
      }}>
        <div style={{ padding: 8, transform: 'scale(1.8)', transformOrigin: '80% 20%' }}>
          {[...Array(5)].map((_,i) => (
            <div key={i} style={{
              height: 1.5, marginBottom: 4,
              width: `${80 - i*10}%`,
              background: 'rgba(28,25,23,0.4)',
            }}/>
          ))}
        </div>
        <svg style={{ position: 'absolute', inset: 0 }}>
          <line x1="60" y1="10" x2="82" y2="10" stroke="var(--accent)" strokeWidth="1"/>
          <line x1="82" y1="10" x2="82" y2="32" stroke="var(--accent)" strokeWidth="1"/>
        </svg>
      </div>
    </div>
  );
}

function EditScreen({ onSave, onAddPage, pageCount = 1 }) {
  const [filter, setFilter] = React.useState('Авто');
  const filters = ['Авто', 'Ч/Б', 'Серый', 'Оригинал'];
  const tools = [
    { Icn: IconCrop, label: 'Обрезка' },
    { Icn: IconRotate, label: 'Поворот' },
    { Icn: IconSparkle, label: 'Улучшить' },
    { Icn: IconWand, label: 'Тени' },
  ];
  const filterBg = {
    'Авто': 'linear-gradient(135deg, #FBF7F0, #E8DEC9)',
    'Ч/Б': 'linear-gradient(135deg, #FFFFFF, #CCC)',
    'Серый': 'linear-gradient(135deg, #EEE, #BBB)',
    'Оригинал': 'linear-gradient(135deg, #F5EFE0, #C9B795)',
  }[filter];

  return (
    <div className="screen">
      <div className="paper-grain"/>
      <StatusBar/>

      {/* Header */}
      <div style={{
        position: 'absolute', top: 54, left: 0, right: 0,
        padding: '14px 16px', display: 'flex', alignItems: 'center',
        justifyContent: 'space-between', zIndex: 10,
      }}>
        <button style={{ width: 40, height: 40, background: 'transparent', border: 0,
          color: 'var(--text-secondary)', cursor: 'pointer' }}>
          <IconArrowLeft size={22}/>
        </button>
        <div style={{ fontFamily: 'var(--display)', fontSize: 20, fontWeight: 600 }}>Редактирование</div>
        <button onClick={onSave} style={{
          padding: '8px 16px', background: 'transparent', border: 0,
          fontFamily: 'var(--body)', fontSize: 15, fontWeight: 500,
          color: 'var(--accent)', cursor: 'pointer',
        }}>Готово</button>
      </div>

      {/* Document preview */}
      <div style={{
        position: 'absolute', top: 124, left: 24, right: 24, height: 420,
        display: 'flex', alignItems: 'center', justifyContent: 'center',
      }}>
        <div style={{
          width: '100%', height: '100%',
          background: filterBg,
          borderRadius: 6,
          boxShadow: 'var(--shadow-md)',
          position: 'relative', overflow: 'hidden',
          transition: 'background 250ms',
        }}>
          <div style={{ padding: '36px 28px' }}>
            <div style={{ fontFamily: 'var(--display)', fontSize: 18, fontWeight: 600,
              color: filter === 'Ч/Б' ? '#000' : 'rgba(28,25,23,0.88)', marginBottom: 14 }}>
              Договор №47/2026
            </div>
            {[...Array(14)].map((_,i) => (
              <div key={i} style={{
                height: 2.5, marginBottom: 8,
                width: `${55 + (i*13 % 40)}%`,
                background: filter === 'Ч/Б' ? '#000' : 'rgba(28,25,23,0.4)',
                borderRadius: 1,
              }}/>
            ))}
          </div>
          {pageCount > 1 && (
            <div style={{
              position: 'absolute', bottom: 10, right: 10,
              padding: '3px 10px', background: 'rgba(28,25,23,0.7)',
              color: '#fff', borderRadius: 4, fontSize: 10,
              fontFamily: 'var(--mono)', letterSpacing: '0.1em',
            }}>{pageCount} СТР</div>
          )}
        </div>
      </div>

      {/* Bottom toolbox */}
      <div style={{
        position: 'absolute', bottom: 0, left: 0, right: 0,
        background: 'var(--surface)',
        borderTopLeftRadius: 20, borderTopRightRadius: 20,
        boxShadow: 'var(--shadow-lg)',
        padding: '18px 0 28px',
      }}>
        {/* Filter chips */}
        <div style={{ padding: '0 20px 16px', display: 'flex', gap: 8, overflowX: 'auto' }}>
          {filters.map(f => (
            <button key={f} className={"chip" + (f === filter ? ' selected' : '')}
              onClick={() => setFilter(f)}>{f}</button>
          ))}
        </div>
        {/* Tools */}
        <div style={{
          display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)',
          padding: '0 16px 16px',
        }}>
          {tools.map((t, i) => (
            <button key={i} style={{
              background: 'transparent', border: 0, padding: '10px 0',
              display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 6,
              color: 'var(--text-secondary)', cursor: 'pointer',
            }}>
              <t.Icn size={22} strokeWidth={1.8}/>
              <span style={{ fontFamily: 'var(--body)', fontSize: 11, fontWeight: 500 }}>{t.label}</span>
            </button>
          ))}
        </div>
        {/* Actions */}
        <div style={{ display: 'flex', gap: 10, padding: '0 16px' }}>
          <button className="btn btn-secondary" onClick={onAddPage} style={{ flex: 1 }}>
            + Страница
          </button>
          <button className="btn btn-primary" onClick={onSave} style={{ flex: 1.4 }}>
            Сохранить всё
          </button>
        </div>
      </div>
      <HomeIndicator/>
    </div>
  );
}

function SaveSheet({ onClose, onSave }) {
  const [name, setName] = React.useState('');
  const [fmt, setFmt] = React.useState('PDF');
  const placeholder = "Скан 24.04.2026 14:35";
  return (
    <>
      <div className="sheet-backdrop" onClick={onClose}/>
      <div className="bottom-sheet">
        <div className="sheet-handle"/>
        <div style={{ padding: '8px 24px 0' }}>
          <h2 style={{ fontFamily: 'var(--display)', fontSize: 24, fontWeight: 600,
            margin: 0, letterSpacing: '0.01em' }}>Сохранить документ</h2>
          <p style={{ fontFamily: 'var(--body)', fontWeight: 300, fontSize: 14,
            color: 'var(--text-secondary)', marginTop: 6 }}>
            Файл будет сохранён локально на устройстве.
          </p>

          {/* Name input */}
          <div style={{ marginTop: 24 }}>
            <div style={{ fontFamily: 'var(--body)', fontSize: 12, fontWeight: 500,
              color: 'var(--text-secondary)', marginBottom: 8,
              letterSpacing: '0.05em', textTransform: 'uppercase' }}>
              Название
            </div>
            <input
              value={name} onChange={e => setName(e.target.value)}
              placeholder={placeholder}
              style={{
                width: '100%', padding: '14px 16px',
                background: 'var(--bg-secondary)',
                border: '1px solid var(--bg-tertiary)',
                borderRadius: 12, outline: 'none',
                fontFamily: name ? 'var(--body)' : 'var(--mono)', fontSize: 15,
                color: 'var(--text-primary)',
              }}/>
          </div>

          {/* Format */}
          <div style={{ marginTop: 20 }}>
            <div style={{ fontFamily: 'var(--body)', fontSize: 12, fontWeight: 500,
              color: 'var(--text-secondary)', marginBottom: 8,
              letterSpacing: '0.05em', textTransform: 'uppercase' }}>
              Формат
            </div>
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr 1fr', gap: 8 }}>
              {['PDF','JPG','PNG'].map(f => (
                <button key={f} onClick={() => setFmt(f)} style={{
                  padding: '14px 0',
                  background: fmt === f ? 'var(--accent)' : 'var(--accent-subtle)',
                  color: fmt === f ? '#fff' : 'var(--accent)',
                  border: 0, borderRadius: 12,
                  fontFamily: 'var(--body)', fontSize: 13, fontWeight: 500,
                  letterSpacing: '0.1em', cursor: 'pointer',
                  transition: 'all 150ms',
                }}>{f}</button>
              ))}
            </div>
          </div>

          {/* Folder */}
          <button style={{
            marginTop: 12, width: '100%',
            padding: '14px 16px',
            background: 'var(--bg-secondary)',
            border: '1px solid var(--bg-tertiary)',
            borderRadius: 12,
            display: 'flex', alignItems: 'center', gap: 12, cursor: 'pointer',
          }}>
            <IconFolder size={20} stroke="var(--accent)" strokeWidth={1.8}/>
            <span style={{ flex: 1, textAlign: 'left', fontFamily: 'var(--body)',
              fontSize: 15, color: 'var(--text-primary)' }}>Выбрать папку</span>
            <span style={{ fontFamily: 'var(--mono)', fontSize: 12,
              color: 'var(--text-tertiary)' }}>Без папки</span>
            <IconChevronRight size={16} stroke="var(--text-tertiary)" strokeWidth={1.8}/>
          </button>

          <button className="btn btn-primary" onClick={onSave}
            style={{ width: '100%', marginTop: 20 }}>
            Сохранить
          </button>
        </div>
      </div>
    </>
  );
}

Object.assign(window, { CameraScreen, CropScreen, EditScreen, SaveSheet });

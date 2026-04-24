// screens-settings.jsx — Settings, Paywall, Limit-reached sheet

function SettingsScreen({ onBack, onTab, active, onScan, isPremium, setPremium, scansUsed, onPaywall }) {
  return (
    <div className="screen">
      <div className="paper-grain"/>
      <StatusBar/>
      <div style={{ flex: 1, overflowY: 'auto', paddingTop: 54, paddingBottom: 110 }}>
        {/* Large title */}
        <div style={{ padding: '20px 24px 24px' }}>
          <div style={{ fontFamily: 'var(--mono)', fontSize: 10,
            letterSpacing: '0.15em', textTransform: 'uppercase',
            color: 'var(--text-tertiary)', marginBottom: 4 }}>Settings</div>
          <h1 style={{ fontFamily: 'var(--display)', fontWeight: 300, fontSize: 38,
            margin: 0, letterSpacing: '0.01em' }}>Настройки</h1>
        </div>

        {/* Subscription card */}
        <div style={{ padding: '0 20px 20px' }}>
          <div style={{
            background: isPremium
              ? 'linear-gradient(140deg, #F7F0E2 0%, #EAE0CA 100%)'
              : 'var(--surface-raised)',
            border: isPremium ? '1.5px solid var(--accent)' : '1px solid var(--bg-tertiary)',
            borderRadius: 16, padding: 22,
            boxShadow: 'var(--shadow-sm)', position: 'relative', overflow: 'hidden',
          }}>
            {isPremium && (
              <div style={{
                position: 'absolute', top: -24, right: -24,
                width: 100, height: 100, borderRadius: '50%',
                background: 'radial-gradient(circle, rgba(196,168,130,0.3), transparent 70%)',
              }}/>
            )}
            <div style={{ display: 'flex', alignItems: 'center', gap: 8, marginBottom: 10 }}>
              {isPremium && <IconCrown size={14} stroke="var(--accent)" strokeWidth={2}/>}
              <span style={{
                fontFamily: 'var(--display)', fontStyle: 'italic', fontSize: 13,
                fontWeight: 500, letterSpacing: '0.15em', textTransform: 'uppercase',
                color: 'var(--accent)',
              }}>{isPremium ? 'Премиум активен' : 'Бесплатный план'}</span>
            </div>
            {isPremium ? (
              <>
                <div style={{ display: 'flex', alignItems: 'baseline', gap: 4 }}>
                  <span style={{ fontFamily: 'var(--display)', fontWeight: 700, fontSize: 32,
                    color: 'var(--accent)' }}>149 ₽</span>
                  <span style={{ fontFamily: 'var(--body)', fontSize: 15,
                    color: 'var(--text-secondary)' }}>/ месяц</span>
                </div>
                <div style={{ fontFamily: 'var(--body)', fontWeight: 300, fontSize: 13,
                  color: 'var(--text-secondary)', marginTop: 6, lineHeight: 1.5 }}>
                  Следующее списание · 24 мая 2026.<br/>
                  Подписка продлевается автоматически.
                </div>
                <button onClick={() => setPremium(false)} style={{
                  marginTop: 16, padding: '10px 0', width: '100%',
                  background: 'transparent', border: '1px solid var(--accent)',
                  borderRadius: 10, color: 'var(--accent)',
                  fontFamily: 'var(--body)', fontSize: 13, fontWeight: 500, cursor: 'pointer',
                }}>Управление подпиской</button>
              </>
            ) : (
              <>
                <div style={{ fontFamily: 'var(--display)', fontWeight: 400, fontSize: 22,
                  lineHeight: 1.3, letterSpacing: '0.01em', marginBottom: 10 }}>
                  Сканируйте без ограничений
                </div>
                <div style={{ fontFamily: 'var(--body)', fontSize: 13, fontWeight: 300,
                  color: 'var(--text-secondary)', marginBottom: 14, lineHeight: 1.5 }}>
                  Использовано сегодня: {scansUsed} из 10 сканов. Премиум снимает лимит и открывает пакетное сканирование.
                </div>
                <div style={{ height: 3, background: 'var(--bg-tertiary)',
                  borderRadius: 2, marginBottom: 16 }}>
                  <div style={{ height: '100%', width: `${(scansUsed/10)*100}%`,
                    background: 'var(--accent)', borderRadius: 2 }}/>
                </div>
                <button className="btn btn-primary" onClick={onPaywall}
                  style={{ width: '100%' }}>Улучшить до Премиум</button>
              </>
            )}
          </div>
        </div>

        {/* Settings groups */}
        <SettingsGroup title="Приложение">
          <SettingsRow label="Язык" value="Русский"/>
          <SettingsRow label="Формат экспорта" value="PDF"/>
          <SettingsRow label="Качество" value="Высокое"/>
          <SettingsRow label="Тёмная тема" toggle value={false}/>
        </SettingsGroup>

        <SettingsGroup title="Обработка">
          <SettingsRow label="Авто-захват" toggle value={true}/>
          <SettingsRow label="Удалять тени" toggle value={true}/>
          <SettingsRow label="OCR при сохранении" toggle value={true}/>
        </SettingsGroup>

        <SettingsGroup title="Хранение">
          <SettingsRow label="Занято на устройстве" value="47,2 МБ"/>
          <SettingsRow label="Всего документов" value="6"/>
          <SettingsRow label="Очистить кэш" value="" danger/>
        </SettingsGroup>

        <SettingsGroup title="О приложении">
          <SettingsRow label="Версия" value="2.0 · build 142" mono/>
          <SettingsRow label="Политика приватности"/>
          <SettingsRow label="Условия использования"/>
        </SettingsGroup>

        <div style={{
          padding: '24px 32px 20px', textAlign: 'center',
          fontFamily: 'var(--mono)', fontSize: 10,
          color: 'var(--text-tertiary)', letterSpacing: '0.12em',
        }}>АРХИВ · ОФЛАЙН · БЕЗ АККАУНТА</div>
      </div>

      <TabBar active={active} onTab={onTab} onScan={onScan}/>
      <HomeIndicator/>
    </div>
  );
}

function SettingsGroup({ title, children }) {
  return (
    <div style={{ marginBottom: 20 }}>
      <div style={{
        padding: '0 24px 8px',
        fontFamily: 'var(--body)', fontSize: 11, fontWeight: 500,
        letterSpacing: '0.15em', textTransform: 'uppercase',
        color: 'var(--text-tertiary)',
      }}>{title}</div>
      <div style={{
        margin: '0 20px', background: 'var(--surface-raised)',
        border: '1px solid var(--bg-tertiary)', borderRadius: 14,
        overflow: 'hidden',
      }}>{children}</div>
    </div>
  );
}

function SettingsRow({ label, value, toggle, mono, danger }) {
  const [on, setOn] = React.useState(typeof value === 'boolean' ? value : false);
  return (
    <div style={{
      display: 'flex', alignItems: 'center', gap: 12,
      padding: '14px 16px', cursor: toggle ? 'default' : 'pointer',
      borderBottom: '1px solid var(--bg-tertiary)',
    }} className="settings-row">
      <span style={{
        flex: 1, fontFamily: 'var(--body)', fontSize: 15,
        color: danger ? 'var(--danger)' : 'var(--text-primary)', fontWeight: 400,
      }}>{label}</span>
      {toggle ? (
        <button onClick={() => setOn(!on)} style={{
          width: 42, height: 26, borderRadius: 13,
          background: on ? 'var(--accent)' : 'var(--bg-tertiary)',
          border: 0, padding: 0, cursor: 'pointer',
          position: 'relative', transition: 'background 200ms',
        }}>
          <div style={{
            position: 'absolute', top: 2, left: on ? 18 : 2,
            width: 22, height: 22, borderRadius: '50%',
            background: '#fff', boxShadow: '0 1px 3px rgba(0,0,0,0.2)',
            transition: 'left 200ms ease',
          }}/>
        </button>
      ) : (
        <>
          {value && <span style={{
            fontFamily: mono ? 'var(--mono)' : 'var(--body)', fontSize: 13,
            color: 'var(--text-tertiary)',
          }}>{value}</span>}
          <IconChevronRight size={16} stroke="var(--text-tertiary)" strokeWidth={1.8}/>
        </>
      )}
    </div>
  );
}

function PaywallScreen({ onClose, onPurchase }) {
  const [plan, setPlan] = React.useState('year');
  const features = [
    "Безлимитное сканирование",
    "Пакетная обработка нескольких страниц",
    "Поиск по тексту OCR внутри документов",
    "Авто-определение типа (чек, визитка, паспорт)",
    "Без рекламы и приоритетная поддержка",
  ];
  return (
    <div className="screen">
      <div className="paper-grain"/>
      <StatusBar/>

      {/* close button */}
      <button onClick={onClose} style={{
        position: 'absolute', top: 60, left: 16, zIndex: 10,
        width: 40, height: 40, background: 'transparent', border: 0,
        color: 'var(--text-secondary)', cursor: 'pointer',
      }}><IconX size={22}/></button>

      <div style={{ flex: 1, overflowY: 'auto', paddingTop: 104, paddingBottom: 40 }}>
        <div style={{ padding: '0 28px' }}>
          {/* Hero */}
          <div style={{ textAlign: 'center', marginBottom: 32 }}>
            <div style={{
              width: 56, height: 56, borderRadius: 16,
              background: 'var(--accent-subtle)',
              display: 'inline-flex', alignItems: 'center', justifyContent: 'center',
              marginBottom: 20,
              border: '1px solid rgba(139,115,85,0.2)',
            }}>
              <IconCrown size={26} stroke="var(--accent)" strokeWidth={1.8}/>
            </div>
            <div style={{
              fontFamily: 'var(--display)', fontStyle: 'italic', fontSize: 14,
              letterSpacing: '0.2em', textTransform: 'uppercase',
              color: 'var(--accent)', marginBottom: 12,
            }}>Архив · Premium</div>
            <h1 style={{
              fontFamily: 'var(--display)', fontWeight: 500, fontSize: 36,
              margin: 0, lineHeight: 1.1, letterSpacing: '0.01em',
            }}>Без ограничений.<br/><em>Без рекламы.</em></h1>
            <p style={{
              fontFamily: 'var(--body)', fontWeight: 300, fontSize: 15,
              color: 'var(--text-secondary)', marginTop: 14, lineHeight: 1.5,
            }}>Премиум — это редакторский набор инструментов для тех, кто работает с бумагой серьёзно.</p>
          </div>

          {/* Features */}
          <div style={{ marginBottom: 28 }}>
            {features.map((f, i) => (
              <div key={i} style={{
                display: 'flex', alignItems: 'flex-start', gap: 14,
                padding: '10px 0',
                borderBottom: i < features.length - 1 ? '1px solid var(--bg-tertiary)' : 'none',
              }}>
                <div style={{
                  width: 22, height: 22, borderRadius: '50%',
                  background: 'var(--accent-subtle)',
                  display: 'flex', alignItems: 'center', justifyContent: 'center',
                  flexShrink: 0, marginTop: 1,
                }}>
                  <IconCheck size={14} stroke="var(--accent)" strokeWidth={2.4}/>
                </div>
                <span style={{ fontFamily: 'var(--body)', fontSize: 15,
                  color: 'var(--text-primary)', lineHeight: 1.4 }}>{f}</span>
              </div>
            ))}
          </div>

          {/* Plans */}
          <div style={{ display: 'flex', flexDirection: 'column', gap: 10, marginBottom: 20 }}>
            <PlanCard selected={plan === 'month'} onClick={() => setPlan('month')}
              label="Месяц" price="149 ₽" sub="/ мес"/>
            <PlanCard selected={plan === 'year'} onClick={() => setPlan('year')}
              label="Год" price="999 ₽" sub="/ год" highlight
              extra={<><span>Выгоднее на 30%</span> · <span className="mono">83 ₽/мес</span></>}/>
          </div>

          <button className="btn btn-primary" onClick={() => onPurchase(plan)}
            style={{ width: '100%', padding: '16px' }}>
            Попробовать Премиум
          </button>
          <div style={{
            fontFamily: 'var(--mono)', fontSize: 10,
            color: 'var(--text-tertiary)', textAlign: 'center',
            letterSpacing: '0.12em', marginTop: 14, textTransform: 'uppercase',
          }}>Демо-версия · покупка не спишется</div>
        </div>
      </div>
      <HomeIndicator/>
    </div>
  );
}

function PlanCard({ selected, onClick, label, price, sub, highlight, extra }) {
  return (
    <button onClick={onClick} style={{
      width: '100%', padding: '18px 20px',
      background: selected ? 'var(--accent-subtle)' : 'var(--surface-raised)',
      border: selected ? '1.5px solid var(--accent)' : '1px solid var(--bg-tertiary)',
      borderRadius: 14, cursor: 'pointer',
      display: 'flex', alignItems: 'center', gap: 16,
      textAlign: 'left', position: 'relative',
      transition: 'all 180ms',
    }}>
      {/* radio */}
      <div style={{
        width: 22, height: 22, borderRadius: '50%',
        border: selected ? '6px solid var(--accent)' : '2px solid var(--bg-tertiary)',
        background: '#fff', flexShrink: 0,
        transition: 'all 180ms',
      }}/>
      <div style={{ flex: 1 }}>
        <div style={{
          fontFamily: 'var(--body)', fontSize: 12, fontWeight: 500,
          letterSpacing: '0.12em', textTransform: 'uppercase',
          color: 'var(--text-secondary)', marginBottom: 4,
        }}>{label}</div>
        {extra && (
          <div style={{
            fontFamily: 'var(--mono)', fontSize: 10, color: 'var(--success)',
            letterSpacing: '0.1em', textTransform: 'uppercase', marginTop: 4,
          }}>{extra}</div>
        )}
      </div>
      <div style={{ textAlign: 'right' }}>
        <span style={{
          fontFamily: 'var(--display)', fontWeight: 700, fontSize: 28,
          color: selected ? 'var(--accent)' : 'var(--text-primary)',
        }}>{price}</span>
        <span style={{
          fontFamily: 'var(--body)', fontSize: 13, fontWeight: 400,
          color: 'var(--text-secondary)', marginLeft: 2,
        }}>{sub}</span>
      </div>
      {highlight && (
        <div style={{
          position: 'absolute', top: -10, right: 16,
          background: 'var(--accent)', color: '#fff',
          padding: '3px 10px', borderRadius: 4,
          fontFamily: 'var(--body)', fontSize: 10, fontWeight: 600,
          letterSpacing: '0.15em', textTransform: 'uppercase',
        }}>Выгодно</div>
      )}
    </button>
  );
}

function LimitReachedSheet({ onClose, onUpgrade }) {
  return (
    <>
      <div className="sheet-backdrop" onClick={onClose}/>
      <div className="bottom-sheet">
        <div className="sheet-handle"/>
        <div style={{ padding: '24px 28px 8px', textAlign: 'center' }}>
          <div style={{
            width: 64, height: 64, borderRadius: 20,
            background: 'var(--accent-subtle)',
            display: 'inline-flex', alignItems: 'center', justifyContent: 'center',
            marginBottom: 16,
          }}>
            <IconCrown size={28} stroke="var(--accent)" strokeWidth={1.8}/>
          </div>
          <h2 style={{
            fontFamily: 'var(--display)', fontSize: 28, fontWeight: 600,
            margin: 0, letterSpacing: '0.01em',
          }}>Дневной лимит достигнут</h2>
          <p style={{
            fontFamily: 'var(--body)', fontSize: 15, fontWeight: 300,
            color: 'var(--text-secondary)', marginTop: 12, lineHeight: 1.5,
          }}>На бесплатном плане — <b style={{ color: 'var(--text-primary)' }}>10 сканов в сутки</b>. Лимит обновится завтра в 00:00, или снимите его премиумом.</p>

          <button className="btn btn-primary" onClick={onUpgrade}
            style={{ width: '100%', marginTop: 22 }}>
            Попробовать Премиум · 149 ₽/мес
          </button>
          <button onClick={onClose} style={{
            background: 'transparent', border: 0, width: '100%',
            padding: '14px 0', marginTop: 4, cursor: 'pointer',
            fontFamily: 'var(--body)', fontSize: 14, color: 'var(--text-tertiary)',
          }}>Нет, спасибо</button>
        </div>
      </div>
    </>
  );
}

Object.assign(window, { SettingsScreen, PaywallScreen, LimitReachedSheet });

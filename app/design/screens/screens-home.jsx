// screens-home.jsx — Home with document list

const MOCK_DOCS = [
  { id: 1, name: "Договор аренды · 2026", date: "24 апр, 14:35", pages: 5, fmt: "PDF", folder: "Юридическое" },
  { id: 2, name: "Квитанция ЖКХ", date: "22 апр, 09:12", pages: 1, fmt: "JPG", folder: "Финансы" },
  { id: 3, name: "Конспект — Экономика", date: "20 апр, 18:47", pages: 12, fmt: "PDF", folder: "Учёба" },
  { id: 4, name: "Паспорт · копия", date: "18 апр, 11:03", pages: 2, fmt: "PDF", folder: "Документы" },
  { id: 5, name: "Чек · АЗС Лукойл", date: "17 апр, 20:28", pages: 1, fmt: "JPG", folder: "Финансы" },
  { id: 6, name: "Рецепт от терапевта", date: "15 апр, 13:14", pages: 1, fmt: "PDF", folder: "Здоровье" },
];

const RECENT = MOCK_DOCS.slice(0, 4);

function DocThumb({ pages = 1, style = {}, size = 'md' }) {
  const W = size === 'lg' ? 100 : 56;
  const H = size === 'lg' ? 130 : 72;
  return (
    <div className="doc-thumb" style={{ width: W, height: H, position: 'relative', ...style }}>
      {pages > 1 && (
        <>
          <div style={{ position: 'absolute', inset: '-3px -4px -1px 4px', background: 'var(--surface-raised)',
            border: '1px solid var(--bg-tertiary)', borderRadius: 'var(--radius-sm)', zIndex: -1 }} />
          <div style={{ position: 'absolute', inset: '-1px 0 -3px 2px', background: 'var(--surface-raised)',
            border: '1px solid var(--bg-tertiary)', borderRadius: 'var(--radius-sm)', zIndex: -1 }} />
        </>
      )}
    </div>
  );
}

function HomeScreen({ onScan, onOpenDoc, onTab, active, viewMode, setViewMode, onSettings, isPremium, scansUsed }) {
  const [query, setQuery] = React.useState('');
  const filtered = MOCK_DOCS.filter(d => d.name.toLowerCase().includes(query.toLowerCase()));

  return (
    <div className="screen">
      <div className="paper-grain" />
      <StatusBar />

      <div style={{ flex: 1, overflowY: 'auto', paddingTop: 54, paddingBottom: 110 }}>
        {/* Header */}
        <div style={{ padding: '20px 24px 4px', display: 'flex', alignItems: 'flex-end', justifyContent: 'space-between' }}>
          <div>
            <div style={{ fontFamily: 'var(--mono)', fontSize: 10, color: 'var(--text-tertiary)',
              letterSpacing: '0.15em', textTransform: 'uppercase', marginBottom: 4 }}>
              Четверг · 24 апреля
            </div>
            <h1 style={{ fontFamily: 'var(--display)', fontWeight: 300, fontSize: 38, margin: 0,
              letterSpacing: '0.01em', color: 'var(--text-primary)', lineHeight: 1 }}>
              Ваши<br/>документы
            </h1>
          </div>
          <div style={{ display: 'flex', gap: 4, paddingBottom: 6 }}>
            <button onClick={() => setViewMode(viewMode === 'list' ? 'grid' : 'list')} style={{
              width: 40, height: 40, border: 0, background: 'transparent',
              display: 'flex', alignItems: 'center', justifyContent: 'center', cursor: 'pointer',
              color: 'var(--text-secondary)',
            }}>
              {viewMode === 'list' ? <IconGrid size={22} /> : <IconList size={22} />}
            </button>
          </div>
        </div>

        {/* Search */}
        <div style={{ padding: '20px 24px 12px' }}>
          <div style={{
            background: 'var(--bg-secondary)',
            border: '1px solid var(--bg-tertiary)',
            borderRadius: 'var(--radius-md)',
            padding: '12px 14px',
            display: 'flex', alignItems: 'center', gap: 10,
          }}>
            <IconSearch size={18} stroke="var(--text-tertiary)" strokeWidth={1.8} />
            <input
              value={query} onChange={e => setQuery(e.target.value)}
              placeholder="Поиск по документам…"
              style={{
                flex: 1, border: 0, background: 'transparent', outline: 'none',
                fontFamily: 'var(--body)', fontSize: 15, color: 'var(--text-primary)',
              }}/>
            {isPremium && (
              <span style={{
                fontFamily: 'var(--mono)', fontSize: 9, letterSpacing: '0.12em',
                color: 'var(--accent)', textTransform: 'uppercase',
              }}>OCR</span>
            )}
          </div>
        </div>

        {/* Scan usage (free plan) */}
        {!isPremium && (
          <div style={{ padding: '4px 24px 16px' }}>
            <div style={{
              padding: '12px 14px', background: 'var(--accent-subtle)',
              borderRadius: 'var(--radius-md)', border: '1px solid rgba(139,115,85,0.12)',
              display: 'flex', alignItems: 'center', gap: 12,
            }}>
              <div style={{ flex: 1 }}>
                <div style={{ fontFamily: 'var(--body)', fontSize: 12, color: 'var(--accent)',
                  letterSpacing: '0.05em', textTransform: 'uppercase', fontWeight: 500, marginBottom: 6 }}>
                  Бесплатный план · {scansUsed} из 10 сканов сегодня
                </div>
                <div style={{ height: 3, background: 'rgba(139,115,85,0.18)', borderRadius: 2 }}>
                  <div style={{ height: '100%', width: `${(scansUsed/10)*100}%`,
                    background: 'var(--accent)', borderRadius: 2, transition: 'width 400ms' }}/>
                </div>
              </div>
              <button onClick={onSettings} style={{
                background: 'transparent', border: 0, padding: 0, cursor: 'pointer',
                fontFamily: 'var(--display)', fontStyle: 'italic', fontSize: 14,
                color: 'var(--accent)', fontWeight: 500,
              }}>Премиум →</button>
            </div>
          </div>
        )}

        {/* Recents */}
        <div style={{ padding: '8px 0 16px' }}>
          <div style={{
            fontFamily: 'var(--body)', fontWeight: 500, fontSize: 11,
            letterSpacing: '0.15em', textTransform: 'uppercase',
            color: 'var(--text-tertiary)', padding: '0 24px 12px',
          }}>Недавние</div>
          <div className="snap-x" style={{ padding: '0 24px 8px' }}>
            {RECENT.map(d => (
              <div key={d.id} onClick={() => onOpenDoc(d)} style={{ cursor: 'pointer', width: 112 }}>
                <DocThumb pages={d.pages} size="lg" />
                <div style={{ padding: '10px 2px 0' }}>
                  <div style={{ fontFamily: 'var(--body)', fontSize: 13, fontWeight: 500,
                    color: 'var(--text-primary)', overflow: 'hidden', textOverflow: 'ellipsis',
                    whiteSpace: 'nowrap' }}>{d.name}</div>
                  <div style={{ fontFamily: 'var(--mono)', fontSize: 10, color: 'var(--text-tertiary)',
                    marginTop: 2 }}>{d.fmt} · {d.pages} стр</div>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* All documents */}
        <div style={{ padding: '0 24px 20px' }}>
          <div style={{
            display: 'flex', alignItems: 'baseline', justifyContent: 'space-between',
            marginBottom: 12,
          }}>
            <span style={{
              fontFamily: 'var(--body)', fontWeight: 500, fontSize: 11,
              letterSpacing: '0.15em', textTransform: 'uppercase',
              color: 'var(--text-tertiary)',
            }}>Все документы · {filtered.length}</span>
            <span style={{ fontFamily: 'var(--display)', fontStyle: 'italic',
              fontSize: 13, color: 'var(--accent)' }}>Папки</span>
          </div>

          {viewMode === 'list' ? (
            <div style={{ display: 'flex', flexDirection: 'column', gap: 10 }}>
              {filtered.map(d => (
                <div key={d.id} className="card" onClick={() => onOpenDoc(d)}
                  style={{ padding: 12, display: 'flex', gap: 14, alignItems: 'center', cursor: 'pointer' }}>
                  <DocThumb pages={d.pages} />
                  <div style={{ flex: 1, minWidth: 0 }}>
                    <div style={{ fontFamily: 'var(--body)', fontSize: 15, fontWeight: 500,
                      color: 'var(--text-primary)', overflow: 'hidden', textOverflow: 'ellipsis',
                      whiteSpace: 'nowrap' }}>{d.name}</div>
                    <div style={{ fontFamily: 'var(--mono)', fontSize: 11, color: 'var(--text-tertiary)',
                      marginTop: 4 }}>{d.date}</div>
                    <div style={{ display: 'flex', gap: 6, marginTop: 8 }}>
                      <span style={{ fontFamily: 'var(--body)', fontSize: 10, fontWeight: 500,
                        letterSpacing: '0.1em', textTransform: 'uppercase', color: 'var(--accent)',
                        background: 'var(--accent-subtle)', padding: '3px 7px', borderRadius: 4 }}>
                        {d.fmt} · {d.pages}{d.pages > 1 ? ' стр' : ' стр'}
                      </span>
                      <span style={{ fontFamily: 'var(--mono)', fontSize: 10, color: 'var(--text-tertiary)',
                        padding: '3px 0' }}>{d.folder}</span>
                    </div>
                  </div>
                  <IconChevronRight size={18} stroke="var(--text-tertiary)" strokeWidth={1.5}/>
                </div>
              ))}
            </div>
          ) : (
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 14 }}>
              {filtered.map(d => (
                <div key={d.id} className="card" onClick={() => onOpenDoc(d)}
                  style={{ padding: 12, cursor: 'pointer' }}>
                  <DocThumb pages={d.pages} size="lg" style={{ width: '100%', height: 170, marginBottom: 10 }}/>
                  <div style={{ fontFamily: 'var(--body)', fontSize: 13, fontWeight: 500,
                    color: 'var(--text-primary)', overflow: 'hidden', textOverflow: 'ellipsis',
                    whiteSpace: 'nowrap' }}>{d.name}</div>
                  <div style={{ fontFamily: 'var(--mono)', fontSize: 10, color: 'var(--text-tertiary)',
                    marginTop: 4 }}>{d.fmt} · {d.pages} стр</div>
                </div>
              ))}
            </div>
          )}

          {filtered.length === 0 && (
            <div style={{ padding: 40, textAlign: 'center' }}>
              <div style={{ fontFamily: 'var(--display)', fontStyle: 'italic', fontSize: 20,
                color: 'var(--text-tertiary)' }}>Ничего не найдено</div>
            </div>
          )}
        </div>
      </div>

      <TabBar active={active} onTab={onTab} onScan={onScan}/>
      <HomeIndicator />
    </div>
  );
}

Object.assign(window, { HomeScreen, DocThumb, MOCK_DOCS });

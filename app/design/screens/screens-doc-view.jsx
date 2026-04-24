// screens-doc-view.jsx — Document viewer, OCR bottom sheet

const SAMPLE_OCR = `ДОГОВОР АРЕНДЫ №47/2026

г. Москва                                24 апреля 2026 г.

Настоящий договор заключён между Арендодателем и Арендатором на условиях, изложенных ниже.

1. ПРЕДМЕТ ДОГОВОРА
Арендодатель передаёт, а Арендатор принимает во временное пользование жилое помещение, расположенное по адресу: г. Москва, ул. Тверская, д. 12, кв. 47.

2. СРОК ДОГОВОРА
Договор заключён на срок 11 месяцев с момента подписания сторонами.

3. РАЗМЕР АРЕНДНОЙ ПЛАТЫ
Ежемесячная арендная плата составляет 85 000 (восемьдесят пять тысяч) рублей.`;

function DocViewScreen({ doc, onBack, onEdit, onDelete, onShare }) {
  const [page, setPage] = React.useState(1);
  const [showOCR, setShowOCR] = React.useState(false);
  const [showMenu, setShowMenu] = React.useState(false);
  const totalPages = doc?.pages || 1;

  return (
    <div className="screen">
      <div className="paper-grain"/>
      <StatusBar/>

      {/* Top bar */}
      <div style={{
        position: 'absolute', top: 54, left: 0, right: 0,
        padding: '12px 8px 12px 8px', zIndex: 10,
        display: 'flex', alignItems: 'center', justifyContent: 'space-between',
      }}>
        <button onClick={onBack} style={{
          width: 40, height: 40, background: 'transparent', border: 0,
          color: 'var(--text-secondary)', cursor: 'pointer',
        }}><IconArrowLeft size={22}/></button>
        <div style={{ flex: 1, minWidth: 0, padding: '0 8px' }}>
          <div style={{
            fontFamily: 'var(--display)', fontSize: 18, fontWeight: 600,
            overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap',
            textAlign: 'center', letterSpacing: '0.01em',
          }}>{doc?.name || 'Документ'}</div>
          <div style={{
            fontFamily: 'var(--mono)', fontSize: 10,
            color: 'var(--text-tertiary)', textAlign: 'center',
            letterSpacing: '0.1em', marginTop: 2,
          }}>{doc?.date || '—'}</div>
        </div>
        <button onClick={() => setShowMenu(!showMenu)} style={{
          width: 40, height: 40, background: 'transparent', border: 0,
          color: 'var(--text-secondary)', cursor: 'pointer',
        }}><IconDots size={22}/></button>
      </div>

      {/* Document page card */}
      <div style={{
        position: 'absolute', top: 128, left: 0, right: 0, bottom: 170,
        display: 'flex', alignItems: 'center', justifyContent: 'center',
        padding: '0 24px',
      }}>
        <div key={page} style={{
          width: '100%', maxWidth: 320, aspectRatio: '0.74',
          background: '#FDFBF8',
          borderRadius: 8, boxShadow: 'var(--shadow-lg)',
          position: 'relative', overflow: 'hidden',
          animation: 'fadeIn 280ms ease-out both',
        }}>
          <div className="page-curl"/>
          <div style={{ padding: '36px 28px' }}>
            <div style={{ fontFamily: 'var(--display)', fontSize: 17, fontWeight: 600,
              color: 'rgba(28,25,23,0.92)', marginBottom: 16 }}>
              Договор №47/2026 · стр. {page}
            </div>
            {[...Array(14)].map((_,i) => (
              <div key={i} style={{
                height: 2.5, marginBottom: 9,
                width: `${55 + ((i + page*3)*13 % 40)}%`,
                background: 'rgba(28,25,23,0.35)', borderRadius: 1,
              }}/>
            ))}
          </div>
        </div>

        {/* Swipe arrows */}
        {page > 1 && (
          <button onClick={() => setPage(page - 1)} style={{
            position: 'absolute', left: 8, top: '50%', transform: 'translateY(-50%)',
            width: 36, height: 36, borderRadius: 18,
            background: 'var(--surface)', border: '1px solid var(--bg-tertiary)',
            boxShadow: 'var(--shadow-sm)', cursor: 'pointer',
            color: 'var(--accent)',
          }}><IconArrowLeft size={18}/></button>
        )}
        {page < totalPages && (
          <button onClick={() => setPage(page + 1)} style={{
            position: 'absolute', right: 8, top: '50%', transform: 'translateY(-50%)',
            width: 36, height: 36, borderRadius: 18,
            background: 'var(--surface)', border: '1px solid var(--bg-tertiary)',
            boxShadow: 'var(--shadow-sm)', cursor: 'pointer',
            color: 'var(--accent)',
          }}><IconArrowRight size={18}/></button>
        )}
      </div>

      {/* Page counter */}
      <div style={{
        position: 'absolute', bottom: 130, left: 0, right: 0,
        fontFamily: 'var(--mono)', fontSize: 12,
        color: 'var(--accent-light)', textAlign: 'center',
        letterSpacing: '0.15em',
      }}>{page} / {totalPages}</div>

      {/* Bottom action bar */}
      <div style={{
        position: 'absolute', bottom: 0, left: 0, right: 0,
        background: 'var(--surface)',
        borderTop: '1px solid var(--bg-tertiary)',
        paddingBottom: 24,
      }}>
        <div style={{
          display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)',
          padding: '14px 8px 10px',
        }}>
          {[
            { Icn: IconEdit, label: 'Изменить', c: 'var(--accent)', onClick: onEdit },
            { Icn: IconText, label: 'Текст', c: 'var(--accent)', onClick: () => setShowOCR(true) },
            { Icn: IconShare, label: 'Поделиться', c: 'var(--accent)', onClick: onShare },
            { Icn: IconTrash, label: 'Удалить', c: 'var(--danger)', onClick: onDelete },
          ].map((a, i) => (
            <button key={i} onClick={a.onClick} style={{
              background: 'transparent', border: 0, cursor: 'pointer',
              display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 6,
              padding: '6px 0', color: a.c,
            }}>
              <a.Icn size={22} strokeWidth={1.7}/>
              <span style={{ fontFamily: 'var(--body)', fontSize: 11, fontWeight: 500,
                color: 'var(--text-secondary)' }}>{a.label}</span>
            </button>
          ))}
        </div>
      </div>
      <HomeIndicator/>

      {showOCR && <OCRSheet onClose={() => setShowOCR(false)}/>}
      {showMenu && (
        <>
          <div style={{ position: 'absolute', inset: 0, zIndex: 90 }} onClick={() => setShowMenu(false)}/>
          <div style={{
            position: 'absolute', top: 100, right: 12, zIndex: 100,
            background: 'var(--surface)', borderRadius: 12,
            boxShadow: 'var(--shadow-lg)', minWidth: 200,
            padding: 4, animation: 'fadeIn 180ms ease-out both',
            border: '1px solid var(--bg-tertiary)',
          }}>
            {['Переименовать','Переместить','Копировать','Распечатать'].map(l => (
              <button key={l} style={{
                display: 'block', width: '100%', padding: '12px 14px',
                background: 'transparent', border: 0, textAlign: 'left',
                fontFamily: 'var(--body)', fontSize: 14,
                color: 'var(--text-primary)', cursor: 'pointer',
              }}>{l}</button>
            ))}
          </div>
        </>
      )}
    </div>
  );
}

function OCRSheet({ onClose }) {
  const [copied, setCopied] = React.useState(false);
  const copy = () => { setCopied(true); setTimeout(() => setCopied(false), 1500); };
  return (
    <>
      <div className="sheet-backdrop" onClick={onClose}/>
      <div className="bottom-sheet" style={{ maxHeight: '72%' }}>
        <div className="sheet-handle"/>
        <div style={{
          padding: '8px 24px 16px',
          display: 'flex', alignItems: 'center', justifyContent: 'space-between',
          borderBottom: '1px solid var(--bg-tertiary)',
        }}>
          <div>
            <div style={{ fontFamily: 'var(--display)', fontSize: 22, fontWeight: 600,
              letterSpacing: '0.01em' }}>Распознанный текст</div>
            <div style={{ fontFamily: 'var(--mono)', fontSize: 10,
              color: 'var(--text-tertiary)', letterSpacing: '0.12em',
              textTransform: 'uppercase', marginTop: 2 }}>
              ML Kit · OCR · Русский / Latin
            </div>
          </div>
          <button onClick={copy} style={{
            display: 'flex', alignItems: 'center', gap: 6,
            padding: '8px 12px', background: 'var(--accent-subtle)',
            color: 'var(--accent)', border: 0, borderRadius: 8,
            fontFamily: 'var(--body)', fontSize: 12, fontWeight: 500, cursor: 'pointer',
          }}>
            {copied ? <IconCheck size={14}/> : <IconCopy size={14}/>}
            {copied ? 'Готово' : 'Копировать'}
          </button>
        </div>
        <div style={{
          padding: '18px 24px 8px', fontFamily: 'var(--body)', fontSize: 14,
          fontWeight: 300, lineHeight: 1.6, color: 'var(--text-primary)',
          whiteSpace: 'pre-wrap', overflowY: 'auto', maxHeight: 340,
        }}>{SAMPLE_OCR}</div>
      </div>
    </>
  );
}

Object.assign(window, { DocViewScreen, OCRSheet });
